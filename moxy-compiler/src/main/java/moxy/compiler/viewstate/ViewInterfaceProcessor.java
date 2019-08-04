package moxy.compiler.viewstate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import moxy.compiler.ElementProcessor;
import moxy.compiler.MvpCompiler;
import moxy.compiler.Util;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public class ViewInterfaceProcessor extends ElementProcessor<TypeElement, moxy.compiler.viewstate.ViewInterfaceInfo> {

    private static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();

    private static final TypeElement NEW_DEFAULT_STATE_STRATEGY = MvpCompiler.getElementUtils()
        .getTypeElement(AddToEndSingleStrategy.class.getCanonicalName());

    private final TypeElement frameworkDefaultStrategy;

    private final boolean disableEmptyStrategyCheck;

    private final boolean enableEmptyStrategyHelper;

    private TypeElement viewInterfaceElement;

    private String viewInterfaceName;

    private List<MigrationMethod> migrationMethods = new ArrayList<>();

    public ViewInterfaceProcessor(
        final boolean disableEmptyStrategyCheck,
        final boolean enableEmptyStrategyHelper,
        final String defaultStrategy
    ) {
        super();
        this.enableEmptyStrategyHelper = enableEmptyStrategyHelper;
        if (defaultStrategy != null) {

            TypeElement localDefaultStrategy = MvpCompiler.getElementUtils().getTypeElement(defaultStrategy);

            if (localDefaultStrategy == null) {
                String message = String
                    .format("Unable to parse option %s. Check %s exists", defaultStrategy, defaultStrategy);

                MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
                localDefaultStrategy = NEW_DEFAULT_STATE_STRATEGY;
            }

            frameworkDefaultStrategy = localDefaultStrategy;
        } else {
            frameworkDefaultStrategy = NEW_DEFAULT_STATE_STRATEGY;
        }
        this.disableEmptyStrategyCheck = disableEmptyStrategyCheck;
    }

    @Override
    public ViewInterfaceInfo process(TypeElement element) {
        this.viewInterfaceElement = element;
        viewInterfaceName = element.getSimpleName().toString();

        List<ViewMethod> methods = new ArrayList<>();

        TypeElement interfaceStateStrategyType = getInterfaceStateStrategyType(element);

        // Get methods for input class
        getMethods(element, interfaceStateStrategyType, new ArrayList<>(), methods);

        // Add methods from super interfaces
        methods.addAll(iterateInterfaces(0, element, interfaceStateStrategyType, methods,
            new ArrayList<>()));

        // Allow methods be with same names
        Map<String, Integer> methodsCounter = new HashMap<>();
        for (ViewMethod method : methods) {
            Integer counter = methodsCounter.get(method.getName());

            if (counter != null && counter > 0) {
                method.setUniqueSuffix(String.valueOf(counter));
            } else {
                counter = 0;
            }

            counter++;
            methodsCounter.put(method.getName(), counter);
        }

        return new moxy.compiler.viewstate.ViewInterfaceInfo(element, methods);
    }

    public JavaFile makeMigrationHelper() {
        if (enableEmptyStrategyHelper && !migrationMethods.isEmpty()) {
            return EmptyStrategyHelperGenerator.generate(migrationMethods);
        }
        return null;
    }

    private void getMethods(TypeElement typeElement,
        TypeElement defaultStrategy,
        List<ViewMethod> rootMethods,
        List<ViewMethod> superinterfacesMethods) {
        for (Element element : typeElement.getEnclosedElements()) {
            // ignore all but non-static methods
            if (element.getKind() != ElementKind.METHOD || element.getModifiers()
                .contains(Modifier.STATIC)) {
                continue;
            }

            final ExecutableElement methodElement = (ExecutableElement) element;

            if (methodElement.getReturnType().getKind() != TypeKind.VOID) {
                String message = String.format("You are trying generate ViewState for %s. "
                        + "But %s contains non-void method \"%s\" that return type is %s. ",
                    typeElement.getSimpleName(),
                    typeElement.getSimpleName(),
                    methodElement.getSimpleName(),
                    methodElement.getReturnType()
                );
                MvpCompiler.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, message, methodElement);
            }

            AnnotationMirror annotation = Util
                .getAnnotation(methodElement, STATE_STRATEGY_TYPE_ANNOTATION);

            // get strategy from annotation
            TypeMirror strategyClassFromAnnotation = Util
                .getAnnotationValueAsTypeMirror(annotation, "value");

            TypeElement strategyClass;
            if (strategyClassFromAnnotation != null) {
                strategyClass = (TypeElement) ((DeclaredType) strategyClassFromAnnotation)
                    .asElement();
            } else {
                if (defaultStrategy == null && !disableEmptyStrategyCheck) {

                    if (enableEmptyStrategyHelper) {
                        migrationMethods.add(new MigrationMethod(typeElement, methodElement));
                    } else {
                        String message = "A View method has no strategy! "
                                + "Add @StateStrategyType annotation to this method, or to the View interface. "
                                + "You can also specify default strategy via compiler option.";

                        MvpCompiler.getMessager()
                            .printMessage(Diagnostic.Kind.ERROR, message, methodElement);
                    }
                }

                strategyClass =
                    defaultStrategy != null ? defaultStrategy : frameworkDefaultStrategy;
            }

            // get tag from annotation
            String tagFromAnnotation = Util.getAnnotationValueAsString(annotation, "tag");

            String methodTag;
            if (tagFromAnnotation != null) {
                methodTag = tagFromAnnotation;
            } else {
                methodTag = methodElement.getSimpleName().toString();
            }

            final ViewMethod method = new ViewMethod(
                (DeclaredType) viewInterfaceElement.asType(), methodElement, strategyClass, methodTag);

            if (rootMethods.contains(method)) {
                continue;
            }

            if (superinterfacesMethods.contains(method)) {
                checkStrategyAndTagEquals(method,
                    superinterfacesMethods.get(superinterfacesMethods.indexOf(method)));
                continue;
            }

            superinterfacesMethods.add(method);
        }
    }

    private void checkStrategyAndTagEquals(
        ViewMethod method,
        ViewMethod existingMethod) {
        List<String> differentParts = new ArrayList<>();
        if (!existingMethod.getStrategy().equals(method.getStrategy())) {
            differentParts.add("strategies");
        }
        if (!existingMethod.getTag().equals(method.getTag())) {
            differentParts.add("tags");
        }

        if (!differentParts.isEmpty()) {
            String arguments = method.getParameterSpecs().stream()
                .map(ParameterSpec::toString)
                .collect(Collectors.joining(", "));

            String parts = differentParts.stream().collect(Collectors.joining(" and "));

            throw new IllegalStateException("Both "
                + existingMethod.getEnclosedClassName()
                + " and "
                + method.getEnclosedClassName()
                + " has method "
                + method.getName()
                + "("
                + arguments
                + ")"
                + " with different "
                + parts
                + "."
                + " Override this method in "
                + viewInterfaceName
                + " or make "
                + parts
                + " equals");
        }
    }

    private List<ViewMethod> iterateInterfaces(int level,
        TypeElement parentElement,
        TypeElement parentDefaultStrategy,
        List<ViewMethod> rootMethods,
        List<ViewMethod> superinterfacesMethods) {
        for (TypeMirror typeMirror : parentElement.getInterfaces()) {
            final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

            final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror)
                .getTypeArguments();
            final List<? extends TypeParameterElement> typeParameters = anInterface
                .getTypeParameters();

            if (typeArguments.size() > typeParameters.size()) {
                throw new IllegalArgumentException(
                    "Code generation for interface " + anInterface.getSimpleName()
                        + " failed. Simplify your generics.");
            }

            TypeElement defaultStrategy = parentDefaultStrategy != null ? parentDefaultStrategy
                : getInterfaceStateStrategyType(anInterface);

            getMethods(anInterface, defaultStrategy, rootMethods, superinterfacesMethods);

            iterateInterfaces(level + 1, anInterface, defaultStrategy, rootMethods,
                superinterfacesMethods);
        }

        return superinterfacesMethods;
    }

    private TypeElement getInterfaceStateStrategyType(TypeElement typeElement) {
        AnnotationMirror annotation = Util
            .getAnnotation(typeElement, STATE_STRATEGY_TYPE_ANNOTATION);
        TypeMirror value = Util.getAnnotationValueAsTypeMirror(annotation, "value");
        if (value != null && value.getKind() == TypeKind.DECLARED) {
            return (TypeElement) ((DeclaredType) value).asElement();
        } else {
            return null;
        }
    }
}

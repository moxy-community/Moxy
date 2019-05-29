package moxy.compiler.viewstate;

import com.squareup.javapoet.ParameterSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import moxy.compiler.ElementProcessor;
import moxy.compiler.MvpCompiler;
import moxy.compiler.Util;

public class ViewInterfaceProcessor extends ElementProcessor<TypeElement, moxy.compiler.viewstate.ViewInterfaceInfo> {

    private static final String STATE_STRATEGY_TYPE_ANNOTATION = StateStrategyType.class.getName();

    private static final TypeElement DEFAULT_STATE_STRATEGY = MvpCompiler.getElementUtils()
            .getTypeElement(AddToEndStrategy.class.getCanonicalName());

    private TypeElement viewInterfaceElement;

    private String viewInterfaceName;

    private Set<TypeElement> usedStrategies = new HashSet<>();

    public List<TypeElement> getUsedStrategies() {
        return new ArrayList<>(usedStrategies);
    }

    @Override
    public moxy.compiler.viewstate.ViewInterfaceInfo process(TypeElement element) {
        this.viewInterfaceElement = element;
        viewInterfaceName = element.getSimpleName().toString();

        List<moxy.compiler.viewstate.ViewMethod> methods = new ArrayList<>();

        TypeElement interfaceStateStrategyType = getInterfaceStateStrategyType(element);

        // Get methods for input class
        getMethods(element, interfaceStateStrategyType, new ArrayList<>(), methods);

        // Add methods from super interfaces
        methods.addAll(iterateInterfaces(0, element, interfaceStateStrategyType, methods, new ArrayList<>()));

        // Allow methods be with same names
        Map<String, Integer> methodsCounter = new HashMap<>();
        for (moxy.compiler.viewstate.ViewMethod method : methods) {
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


    private void getMethods(TypeElement typeElement,
            TypeElement defaultStrategy,
            List<moxy.compiler.viewstate.ViewMethod> rootMethods,
            List<moxy.compiler.viewstate.ViewMethod> superinterfacesMethods) {
        for (Element element : typeElement.getEnclosedElements()) {
            // ignore all but non-static methods
            if (element.getKind() != ElementKind.METHOD || element.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }

            final ExecutableElement methodElement = (ExecutableElement) element;

            if (methodElement.getReturnType().getKind() != TypeKind.VOID) {
                String message = String.format("You are trying generate ViewState for %s. " +
                                "But %s contains non-void method \"%s\" that return type is %s. ",
                        typeElement.getSimpleName(),
                        typeElement.getSimpleName(),
                        methodElement.getSimpleName(),
                        methodElement.getReturnType()
                );
                MvpCompiler.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
            }

            AnnotationMirror annotation = Util.getAnnotation(methodElement, STATE_STRATEGY_TYPE_ANNOTATION);

            // get strategy from annotation
            TypeMirror strategyClassFromAnnotation = Util.getAnnotationValueAsTypeMirror(annotation, "value");

            TypeElement strategyClass;
            if (strategyClassFromAnnotation != null) {
                strategyClass = (TypeElement) ((DeclaredType) strategyClassFromAnnotation).asElement();
            } else {
                strategyClass = defaultStrategy != null ? defaultStrategy : DEFAULT_STATE_STRATEGY;
            }

            // get tag from annotation
            String tagFromAnnotation = Util.getAnnotationValueAsString(annotation, "tag");

            String methodTag;
            if (tagFromAnnotation != null) {
                methodTag = tagFromAnnotation;
            } else {
                methodTag = methodElement.getSimpleName().toString();
            }

            // add strategy to list
            usedStrategies.add(strategyClass);

            final moxy.compiler.viewstate.ViewMethod method = new moxy.compiler.viewstate.ViewMethod(
                    (DeclaredType) viewInterfaceElement.asType(), methodElement, strategyClass, methodTag
            );

            if (rootMethods.contains(method)) {
                continue;
            }

            if (superinterfacesMethods.contains(method)) {
                checkStrategyAndTagEquals(method, superinterfacesMethods.get(superinterfacesMethods.indexOf(method)));
                continue;
            }

            superinterfacesMethods.add(method);
        }
    }

    private void checkStrategyAndTagEquals(
            moxy.compiler.viewstate.ViewMethod method, moxy.compiler.viewstate.ViewMethod existingMethod) {
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

            throw new IllegalStateException("Both " + existingMethod.getEnclosedClassName() +
                    " and " + method.getEnclosedClassName() +
                    " has method " + method.getName() + "(" + arguments + ")" +
                    " with different " + parts + "." +
                    " Override this method in " + viewInterfaceName + " or make " + parts + " equals");
        }
    }

    private List<moxy.compiler.viewstate.ViewMethod> iterateInterfaces(int level,
            TypeElement parentElement,
            TypeElement parentDefaultStrategy,
            List<moxy.compiler.viewstate.ViewMethod> rootMethods,
            List<moxy.compiler.viewstate.ViewMethod> superinterfacesMethods) {
        for (TypeMirror typeMirror : parentElement.getInterfaces()) {
            final TypeElement anInterface = (TypeElement) ((DeclaredType) typeMirror).asElement();

            final List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();
            final List<? extends TypeParameterElement> typeParameters = anInterface.getTypeParameters();

            if (typeArguments.size() > typeParameters.size()) {
                throw new IllegalArgumentException("Code generation for interface " + anInterface.getSimpleName()
                        + " failed. Simplify your generics.");
            }

            TypeElement defaultStrategy = parentDefaultStrategy != null ? parentDefaultStrategy
                    : getInterfaceStateStrategyType(anInterface);

            getMethods(anInterface, defaultStrategy, rootMethods, superinterfacesMethods);

            iterateInterfaces(level + 1, anInterface, defaultStrategy, rootMethods, superinterfacesMethods);
        }

        return superinterfacesMethods;
    }

    private TypeElement getInterfaceStateStrategyType(TypeElement typeElement) {
        AnnotationMirror annotation = Util.getAnnotation(typeElement, STATE_STRATEGY_TYPE_ANNOTATION);
        TypeMirror value = Util.getAnnotationValueAsTypeMirror(annotation, "value");
        if (value != null && value.getKind() == TypeKind.DECLARED) {
            return (TypeElement) ((DeclaredType) value).asElement();
        } else {
            return null;
        }
    }
}

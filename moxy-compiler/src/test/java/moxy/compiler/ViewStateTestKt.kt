package moxy.compiler

import org.junit.Test

class ViewStateTestKt : CompilerTest() {

    @Test
    fun testTagsInStateStrategyType() {
        // language=JAVA
        val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            public interface TaggedView extends MvpView {
                
                @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "method2")
                void methodWithoutTag();
                
                @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "customTag")
                void methodWithTag();
            }
        """.toJavaFile()


        val expected = """
            import java.lang.Override;
            import moxy.viewstate.MvpViewState;
            import moxy.viewstate.ViewCommand;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            
            public class TaggedView${"$$"}State extends MvpViewState<TaggedView> implements TaggedView {
                @Override
                public void methodWithoutTag() {
                    MethodWithoutTagCommand methodWithoutTagCommand = new MethodWithoutTagCommand();
                    viewCommands.beforeApply(methodWithoutTagCommand);
            
                    if (hasNotView()) {
                        return;
                    }
            
                    for (TaggedView view : views) {
                        view.methodWithoutTag();
                    }
            
                    viewCommands.afterApply(methodWithoutTagCommand);
                }
            
                @Override
                public void methodWithTag() {
                    MethodWithTagCommand methodWithTagCommand = new MethodWithTagCommand();
                    viewCommands.beforeApply(methodWithTagCommand);
            
                    if (hasNotView()) {
                        return;
                    }
            
                    for (TaggedView view : views) {
                        view.methodWithTag();
                    }
            
                    viewCommands.afterApply(methodWithTagCommand);
                }
            
                public class MethodWithoutTagCommand extends ViewCommand<TaggedView> {
                    MethodWithoutTagCommand() {
                        super("method2", AddToEndSingleStrategy.class);
                    }
            
                    @Override
                    public void apply(TaggedView mvpView) {
                        mvpView.methodWithoutTag();
                    }
                }
            
                public class MethodWithTagCommand extends ViewCommand<TaggedView> {
                    MethodWithTagCommand() {
                        super("customTag", AddToEndSingleStrategy.class);
                    }
            
                    @Override
                    public void apply(TaggedView mvpView) {
                        mvpView.methodWithTag();
                    }
                }
            }
        """.toJavaFile()


        val compilation = compileSourcesWithProcessor(view, generateViewStateFor("TaggedView"))
        compilation.assertSucceededWithoutWarnings()

        assertExceptedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(view, expected).generatedFiles()
        )
    }

    @Test
    fun testStateStrategyNotInherited() {
        // language=JAVA
        val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(value = AddToEndSingleStrategy.class)
            interface ParentView extends MvpView {
                void parentMethod();
            }
            
            public interface ChildView extends ParentView {
                void childMethod();
            }
        """.toJavaFile()


        compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))
            .assertThatIt()
            .hadErrorContaining("A View method has no strategy!")
    }

    @Test
    fun testStaticMethodInViewInterfaceNotCounted() {
        // language=JAVA
        val view = """
            import moxy.MvpView;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            import moxy.viewstate.strategy.StateStrategyType;
            
            @StateStrategyType(AddToEndSingleStrategy.class)
            public interface StaticView extends MvpView {
            
                static int staticMethod() { return 1; }
                
                void childMethod();
            }
        """.toJavaFile()

        val expected = """
            import java.lang.Override;
            import moxy.viewstate.MvpViewState;
            import moxy.viewstate.ViewCommand;
            import moxy.viewstate.strategy.AddToEndSingleStrategy;
            
            public class StaticView${"$$"}State extends MvpViewState<StaticView> implements StaticView {
                @Override
                public void childMethod() {
                    ChildMethodCommand childMethodCommand = new ChildMethodCommand();
                    this.viewCommands.beforeApply(childMethodCommand);
            
                    if (hasNotView()) {
                        return;
                    }
            
                    for (StaticView view : this.views) {
                        view.childMethod();
                    }
            
                    this.viewCommands.afterApply(childMethodCommand);
                }
            
                public class ChildMethodCommand extends ViewCommand<StaticView> {
                    ChildMethodCommand() {
                        super("childMethod", AddToEndSingleStrategy.class);
                    }
            
                    @Override
                    public void apply(StaticView mvpView) {
                        mvpView.childMethod();
                    }
                }
            }
        """.toJavaFile()

        val compilation = compileSourcesWithProcessor(view, generateViewStateFor(view.name.substringBefore('.')))
        compilation.assertSucceededWithoutWarnings()

        assertExceptedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(view, expected).generatedFiles()
        )
    }
}
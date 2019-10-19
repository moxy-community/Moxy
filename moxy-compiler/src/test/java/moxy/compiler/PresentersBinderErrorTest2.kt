package moxy.compiler

import org.junit.Test

class PresentersBinderErrorTest2 : CompilerTest() {

    @Test
    fun injectPresenterWithoutEmptyConstructor() {
        // language=JAVA
        val view = """
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
            
            public class SomeView implements MvpView {

                @InjectPresenter
                public PresenterWithoutEmptyConstructor presenter;
            }
        """.toJavaFile()

        // language=JAVA
        val presenter = """
            import moxy.MvpView;
            
            public class PresenterWithoutEmptyConstructor extends moxy.MvpPresenter<MvpView> {
            
                public PresenterWithoutEmptyConstructor(String s) {}
            }
        """.toJavaFile()


        val expected =
            """
            import java.lang.IllegalStateException;
            import java.lang.Override;
            import java.util.ArrayList;
            import java.util.List;
            import moxy.MvpPresenter;
            import moxy.PresenterBinder;
            import moxy.presenter.PresenterField;
            
            public class SomeView${"$$"}PresentersBinder extends PresenterBinder<SomeView> {
                @Override
                public List<PresenterField<? super SomeView>> getPresenterFields() {
                    List<PresenterField<? super SomeView>> presenters = new ArrayList<>(1);
                    presenters.add(new PresenterBinder());
                    return presenters;
                }
            
                public class PresenterBinder extends PresenterField<SomeView> {
                    public PresenterBinder() {
                        super("presenter", null, PresenterWithoutEmptyConstructor.class);
                    }
            
                    @Override
                    public void bind(SomeView target, MvpPresenter presenter) {
                        target.presenter = (PresenterWithoutEmptyConstructor) presenter;
                    }
            
                    @Override
                    public MvpPresenter<?> providePresenter(SomeView delegated) {
                        throw new IllegalStateException("PresenterWithoutEmptyConstructor" + " hasn't got a default constructor. You can apply @ProvidePresenter to a method which will construct Presenter. Also you can make it default constructor");
                    }
                }
            }
        """.toJavaFile()


        val compilation = compileSourcesWithProcessor(view, presenter)
        val expectedCompilation = compileSources(view, presenter, expected)

        assertExceptedFilesGenerated(
            compilation.generatedFiles(),
            expectedCompilation.generatedFiles())
    }

    @Test
    fun injectPresenterOnNonPresenterField() {
        // language=JAVA
        val view = """
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
    
            public class ObjectInjectPresenterView implements MvpView {
    
                @InjectPresenter
                public Object mObject;
            }
        """.toJavaFile()

        // TODO add clarifying message if @InjectPresenter placed not on MvpPresenter subclass

        compileSourcesWithProcessor(view)
            .assertFailed()
    }
}
package moxy.compiler

import org.junit.Test
import javax.tools.JavaFileObject

class PresentersBinderTagTest : CompilerTest() {

    // language=JAVA
    private val presenter = """
        import moxy.MvpPresenter;
        import moxy.MvpView;
        
        public class MyPresenter extends MvpPresenter<MvpView> {
        }
        """.toJavaFile()

    @Test
    fun testTagBeingGenerated() {
        // language=JAVA
        val target = """
            import moxy.MvpPresenter;
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
            import moxy.presenter.ProvidePresenterTag;
            
            public class TargetWithCustomTag implements MvpView {
                @InjectPresenter
                MyPresenter presenter;
                
                @ProvidePresenterTag(presenterClass = MyPresenter.class)
                String provideTag() {
                    return Math.random() + "";
                }
            }
        """.toJavaFile()

        val expected = sourceTemplate(provideTagUsed = true)


        val compilation = compileSourcesWithProcessor(target, presenter)
        compilation.assertSucceededWithoutWarnings()
        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(target, presenter, expected).generatedFiles())
    }

    @Test
    fun testStaticTagWithDynamic() {
        // language=JAVA
        val target = """
            import moxy.MvpPresenter;
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
            import moxy.presenter.ProvidePresenterTag;
            
            public class TargetWithCustomTag implements MvpView {
                @InjectPresenter(tag = "staticTag")
                MyPresenter presenter;
                
                @ProvidePresenterTag(presenterClass = MyPresenter.class)
                String provideTag() {
                    return Math.random() + "";
                }
            }
        """.toJavaFile()

        val expected = sourceTemplate(provideTagUsed = true, staticTag = "staticTag")


        val compilation = compileSourcesWithProcessor(target, presenter)
        compilation.assertSucceededWithoutWarnings()
        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(target, presenter, expected).generatedFiles())
    }

    @Test
    fun testProvideTagNotUsedForDifferentPresenterId() {
        // language=JAVA
        val target = """
            import moxy.MvpPresenter;
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
            import moxy.presenter.ProvidePresenterTag;
            
            public class TargetWithCustomTag implements MvpView {
                @InjectPresenter(presenterId = "theOne")
                MyPresenter presenter;
                
                @ProvidePresenterTag(presenterClass = MyPresenter.class)
                String provideTag() {
                    return Math.random() + "";
                }
            }
        """.toJavaFile()

        val expected = sourceTemplate(presenterId = "theOne")


        val compilation = compileSourcesWithProcessor(target, presenter)
        compilation.assertSucceededWithoutWarnings()
        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(target, presenter, expected).generatedFiles())
    }

    @Test
    fun testProvideTagUsedIfPresenterIdEqual() {
        // language=JAVA
        val target = """
            import moxy.MvpPresenter;
            import moxy.MvpView;
            import moxy.presenter.InjectPresenter;
            import moxy.presenter.ProvidePresenterTag;
            
            public class TargetWithCustomTag implements MvpView {
                @InjectPresenter(presenterId = "theOne")
                MyPresenter presenter;
                
                @ProvidePresenterTag(presenterClass = MyPresenter.class, presenterId = "theOne")
                String provideTag() {
                    return Math.random() + "";
                }
            }
        """.toJavaFile()

        val expected = sourceTemplate(provideTagUsed = true, presenterId = "theOne")


        val compilation = compileSourcesWithProcessor(target, presenter)
        compilation.assertSucceededWithoutWarnings()
        assertExpectedFilesGenerated(
            compilation.generatedFiles(),
            compileSources(target, presenter, expected).generatedFiles())
    }

    private fun sourceTemplate(
        provideTagUsed: Boolean = false,
        staticTag: String = "presenter",
        presenterId: String? = null
    ): JavaFileObject {
        val tagMethod = if (provideTagUsed) """
            @Override
            public String getTag(TargetWithCustomTag delegated) {
                return String.valueOf(delegated.provideTag());
            }
        """ else ""

        val presenterId = presenterId?.let { "\"$it\"" } ?: "null"

        return """
        import java.lang.Override;
        import java.lang.String;
        import java.util.ArrayList;
        import java.util.List;
        import moxy.MvpPresenter;
        import moxy.PresenterBinder;
        import moxy.presenter.PresenterField;

        public class TargetWithCustomTag${"$$"}PresentersBinder extends PresenterBinder<TargetWithCustomTag> {
            @Override
            public List<PresenterField<? super TargetWithCustomTag>> getPresenterFields() {
                List<PresenterField<? super TargetWithCustomTag>> presenters = new ArrayList<>(1);
                presenters.add(new PresenterBinder());
                return presenters;
            }
    
            public class PresenterBinder extends PresenterField<TargetWithCustomTag> {
                public PresenterBinder() {
                    super("$staticTag", $presenterId, MyPresenter.class);
                }
    
                @Override
                public void bind(TargetWithCustomTag target, MvpPresenter presenter) {
                    target.presenter = (MyPresenter) presenter;
                }
    
                @Override
                public MvpPresenter<?> providePresenter(TargetWithCustomTag delegated) {
                    return new MyPresenter();
                }
    
                $tagMethod
            }
        }
        """.toJavaFile()
    }
}
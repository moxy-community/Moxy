package moxy.ktx

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import moxy.MvpDelegate
import moxy.MvpPresenter
import moxy.MvpView
import moxy.presenter.PresenterField
import moxy.presenterScope
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PresenterScopeTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `test scope cancelled on destroy`() {

        val (presenter, delegate) = createNewPresenter()
        val job1 = presenter.presenterScope.launch { delay(10_000) }
        val job2 = presenter.presenterScope.launch { delay(10_000) }

        delegate.dispatchFullLifecycle()

        Assert.assertTrue(job1.isCancelled)
        Assert.assertTrue(job2.isCancelled)
    }

    @Test
    fun `test scope cancelled if accessed after onDestroy`() {
        val (presenter, delegate) = createNewPresenter()
        delegate.dispatchFullLifecycle()
        val job1 = presenter.presenterScope.launch { delay(10_000) }
        Assert.assertTrue(job1.isCancelled)
    }

    @Test
    fun `test scope is same`() {
        val (presenter, delegate) = createNewPresenter()
        val scope1 = presenter.presenterScope
        val scope2 = presenter.presenterScope
        Assert.assertEquals(scope1, scope2)

        delegate.dispatchFullLifecycle()

        val scope3 = presenter.presenterScope
        Assert.assertEquals(scope2, scope3)
    }

    @Test
    fun `test job is SupervisorJob`() {
        val (presenter, _) = createNewPresenter()
        val scope = presenter.presenterScope
        val delayingDeferred = scope.async { delay(Long.MAX_VALUE) }
        val failingDeferred = scope.async { throw Error() }
        runBlocking {
            try {
                failingDeferred.await()
            } catch (e: Error) {
            }
            Assert.assertTrue(delayingDeferred.isActive)
            delayingDeferred.cancelAndJoin()
        }
    }

    // Helper functions

    private fun createNewPresenter(): Pair<MvpPresenter<MvpView>, MvpDelegate<MvpView>> {
        val presenter = object : MvpPresenter<MvpView>() {}
        val delegated = object : MvpView {}
        val mvpDelegate = MvpDelegate<MvpView>(delegated)
        mvpDelegate.registerExternalPresenterField(object :
            PresenterField<MvpView>("tag", "id", MvpPresenter::class.java) {
            override fun bind(container: MvpView?, presenter: MvpPresenter<*>?) = Unit
            override fun providePresenter(delegated: MvpView?): MvpPresenter<*> = presenter
        })
        return presenter to mvpDelegate
    }

    private fun MvpDelegate<*>.dispatchFullLifecycle() {
        dispatchCreate()
        dispatchDestroy()
    }

    private fun MvpDelegate<*>.dispatchCreate() {
        onCreate()
        onAttach()
    }

    private fun MvpDelegate<*>.dispatchDestroy() {
        onSaveInstanceState()
        onDetach()
        onDestroyView()
        onDestroy()
    }
}
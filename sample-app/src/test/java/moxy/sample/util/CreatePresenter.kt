package moxy.sample.util

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import moxy.MvpPresenter
import moxy.MvpView

@ExperimentalCoroutinesApi
inline fun <P, reified V> createMockPresenterBlocking(
    noinline createPresenter: () -> P,
    noinline createMocks: (suspend () -> Unit)? = null,
    noinline testState: suspend PVContext<P, V>.() -> Unit
) where P : MvpPresenter<V>, V : MvpView = runBlockingTest {
    createMocks?.invoke()
    val view = mock<V>()
    val presenter = createPresenter()
    presenter.attachView(view)
    testState(PVContext(presenter, view))
    presenter.detachView(view)
}

class PVContext<P, V>(val presenter: P, val view: V) where P : MvpPresenter<V>, V : MvpView
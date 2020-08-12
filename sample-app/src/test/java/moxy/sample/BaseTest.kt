package moxy.sample.dailypicture.ui

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import moxy.MvpPresenter
import moxy.MvpView

open class BaseTest {

    /**
     * Предоставляет контекст с presenter и view, внутри которого можно выполнять проверки
     *
     * @param createPresenter - лямбда для создания презентера
     * @param createMocks - лямбда для инициализации моков для теста
     * @param testState - контекст для проверок тест, предоставляет presenter и view
     *
     */
    inline fun <P, reified V> createMockPresenter(
        createPresenter: () -> P,
        noinline createMocks: (() -> Unit)? = null,
        testState: PVContext<P, V>.() -> Unit
    ) where P : MvpPresenter<V>, V : MvpView {
        createMocks?.invoke()
        val view = mock<V>()
        val presenter = createPresenter()
        presenter.attachView(view)
        testState(PVContext(presenter, view))
        presenter.detachView(view)
    }

    /**
     * Предоставляет блокирущий контекст с presenter и view, внутри которого можно выполнять проверки
     *
     * @param createPresenter - лямбда для создания презентера
     * @param createMocks - лямбда для инициализации моков для теста
     * @param testState - контекст для проверок тест, предоставляет presenter и view
     *
     */
    inline fun <P, reified V> createMockPresenterBlocking(
        noinline createPresenter: suspend () -> P,
        noinline createMocks: (suspend () -> Unit)? = null,
        noinline testState: suspend PVContext<P, V>.() -> Unit
    ) where P : MvpPresenter<V>, V : MvpView = runBlocking {
        createMocks?.invoke()
        val view = mock<V>()
        val presenter = createPresenter()
        presenter.attachView(view)
        testState(PVContext(presenter, view))
        presenter.detachView(view)
    }

    class PVContext<P, V>(val presenter: P, val view: V) where P : MvpPresenter<V>, V : MvpView
}
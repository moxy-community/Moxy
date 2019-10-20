package moxy

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * [CoroutineScope] tied to lifecycle of this [Presenter][MvpPresenter].
 * This scope will be canceled when Presenter will be destroyed, i.e [MvpPresenter.onDestroy] is called
 *
 * This scope is bound to [Dispatchers.Main]
 */
val MvpPresenter<*>.presenterScope: CoroutineScope
    get() {
        val scope = coroutineScope as? CoroutineScope?
        if (scope != null) {
            return scope
        }
        // The fact that cast failed and field is not null means that presenter destroyed and scope must be cancelled
        if (coroutineScope == OnDestroyListener.EMPTY) {
            return CoroutineScope(Job().apply { cancel() })
        }
        return PresenterCoroutineScope().also { coroutineScope = it }
    }

internal class PresenterCoroutineScope : CoroutineScope by MainScope(), OnDestroyListener {
    override fun onDestroy() = coroutineContext.cancel()
}
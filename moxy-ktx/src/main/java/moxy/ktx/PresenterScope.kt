package moxy

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

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
        return PresenterCoroutineScope(SupervisorJob() + Dispatchers.Main).also {
            coroutineScope = it
        }
    }

internal class PresenterCoroutineScope(
    override val coroutineContext: CoroutineContext
) : CoroutineScope, OnDestroyListener {
    override fun onDestroy() = coroutineContext.cancel()
}
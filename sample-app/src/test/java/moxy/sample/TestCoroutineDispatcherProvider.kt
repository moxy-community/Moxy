package moxy.sample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import moxy.sample.util.CoroutineDispatcherProvider

class TestCoroutineDispatcherProvider : CoroutineDispatcherProvider() {
    override val main: CoroutineDispatcher = Dispatchers.Unconfined
    override val default: CoroutineDispatcher = Dispatchers.Unconfined
    override val io: CoroutineDispatcher = Dispatchers.Unconfined
}
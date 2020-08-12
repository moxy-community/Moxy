package moxy.sample.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class CoroutineDispatcherProvider {
    /**
     * Main thread on Android, interact with the UI and perform light work
     *
     * - Calling suspend functions
     * - Call UI functions
     * - Updating LiveData
     */
    open val main: CoroutineDispatcher by lazy { Dispatchers.Main }

    /**
     * Optimized for CPU intensive work off the main thread
     *
     * - Sorting a list
     * - Parsing JSON
     * - DiffUtils
     */
    open val default: CoroutineDispatcher by lazy { Dispatchers.Default }

    /**
     * Optimized for disk and network IO off the main thread
     *
     * - Database
     * - Reading/writing files
     * - Networking
     */
    open val io: CoroutineDispatcher by lazy { Dispatchers.IO }
}
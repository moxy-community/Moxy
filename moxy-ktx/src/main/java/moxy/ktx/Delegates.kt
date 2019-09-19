package moxy.ktx

import moxy.MvpDelegateHolder
import moxy.MvpPresenter

/**
 * Creates a new instance of [MoxyKtxDelegate].
 * Any attempt to access property before [MvpDelegate.onCreate()][moxy.MvpDelegate.onCreate] will throw exception.
 * @param name Unique name of presenter. Every presenter of the same type in single instance of [MvpDelegate][moxy.MvpDelegate] must have unique name.
 * @param factory Presenter initialization function. In will be invoked, when [MvpDelegate.onCreate()][moxy.MvpDelegate.onCreate] is called.
 */
inline fun <reified T : MvpPresenter<*>> MvpDelegateHolder.moxyPresenter(
    name: String = "presenter",
    noinline factory: () -> T
): MoxyKtxDelegate<T> {
    return MoxyKtxDelegate(mvpDelegate, T::class.java.name + "." + name, factory)
}

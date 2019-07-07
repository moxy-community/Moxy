package moxy.ktx

import moxy.MvpDelegateHolder
import moxy.MvpPresenter

inline fun <reified T : MvpPresenter<*>> MvpDelegateHolder.moxyPresenter(
    name: String = "presenter",
    noinline factory: () -> T
): MoxyKtxDelegate<T> {
    return MoxyKtxDelegate(mvpDelegate, T::class.java.name + "." + name, factory)
}

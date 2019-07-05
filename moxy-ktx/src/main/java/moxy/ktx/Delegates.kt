package moxy.ktx

import moxy.MvpDelegateHolder
import moxy.MvpPresenter

fun <T : MvpPresenter<*>> MvpDelegateHolder.moxyPresenter(
    name: String = "presenter",
    factory: () -> T
): MoxyKtxDelegate<T> {
    return MoxyKtxDelegate(mvpDelegate, name, factory)
}

package moxy.ktx

import moxy.MvpDelegateHolder
import moxy.MvpPresenter

fun <T : MvpPresenter<*>> MvpDelegateHolder.moxyPresenter(factory: () -> T) = MoxyKtxDelegate(mvpDelegate, factory)

package moxy.ktx

import moxy.MvpDelegateHolder
import moxy.MvpPresenter

fun <T: MvpPresenter<*>> MvpDelegateHolder.presenter(factory: () -> T) = MoxyKtxDelegate(mvpDelegate, factory)
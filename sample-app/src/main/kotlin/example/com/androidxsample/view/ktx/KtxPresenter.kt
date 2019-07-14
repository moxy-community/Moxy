package example.com.androidxsample.view.ktx

import example.com.androidxsample.view.Logger
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class KtxPresenter constructor(
    private val logger: Logger
) : MvpPresenter<KtxView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        logger.printLog("presenter hash code : ${hashCode()}")
        viewState.printKtxLog("TEST")
    }

    fun printLog() {
        viewState.printKtxLog("TEST ktx print log ${hashCode()}")
    }
}
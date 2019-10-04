package example.com.androidxsample.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
    @StateStrategyType(AddToEndStrategy::class)
    fun printLog(msg: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openKtxActivity()

    @StateStrategyType(AddToEndStrategy::class)
    fun tryView(view: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun tryViewCommands(viewCommands: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun tryViews(views: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun tryTag(tag: String)

}
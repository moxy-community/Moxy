package example.com.androidxsample.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun printLog(msg: String)
}
package example.com.androidxsample.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
    fun printLog(msg: String)
}
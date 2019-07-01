package example.com.androidxsample.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
  @StateStrategyType(AddToEndStrategy::class)
  fun printLog(msg: String)
}
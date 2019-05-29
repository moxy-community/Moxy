package example.com.androidxsample.view

import moxy.MvpView

interface MainView : MvpView {
	fun printLog(msg: String)
}
package example.com.androidxsample.view

import io.moxy.MvpView

interface MainView : MvpView {
	fun printLog(msg: String)
}
package example.com.androidxsample.view

import android.os.Bundle
import android.util.Log
import example.com.androidxsample.R
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = MainPresenter(Logger())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.printLog()
    }

    override fun printLog(msg: String) {
        Log.e(TAG, "printLog : msg : $msg activity hash code : ${hashCode()}")
    }

    companion object {
        const val TAG = "MoxyDebug"
    }
}

package example.com.androidxsample.view

import android.os.Bundle
import android.util.Log
import dagger.Lazy
import dagger.android.AndroidInjection
import example.com.androidxsample.R
import io.moxy.MvpAppCompatActivity
import io.moxy.presenter.InjectPresenter
import io.moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    lateinit var daggerPresenter: Lazy<MainPresenter>

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

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

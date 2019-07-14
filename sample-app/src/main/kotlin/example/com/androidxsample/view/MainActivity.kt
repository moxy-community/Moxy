package example.com.androidxsample.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import example.com.androidxsample.R
import example.com.androidxsample.view.ktx.KtxActivity
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val logger = Logger()

    @ProvidePresenter
    fun providePresenter(): MainPresenter = MainPresenter(logger)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.printLog()

        findViewById<Button>(R.id.activity_main_button_ktx).setOnClickListener {
            presenter.onOpenKtxButtonClick()
        }
    }

    override fun printLog(msg: String) {
        logger.printLog("printLog : msg : $msg activity hash code : ${hashCode()}")
    }

    override fun openKtxActivity() {
        startActivity(Intent(this, KtxActivity::class.java))
    }

}

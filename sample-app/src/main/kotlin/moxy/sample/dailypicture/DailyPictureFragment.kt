package moxy.sample.dailypicture

import example.com.androidxsample.R
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class DailyPictureFragment : MvpAppCompatFragment(R.layout.fragment_daily_picture),
    DailyPictureView {

    private val presenter: DailyPicturePresenter by moxyPresenter { DailyPicturePresenter() }

    override fun showPicture(url: String) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }
}

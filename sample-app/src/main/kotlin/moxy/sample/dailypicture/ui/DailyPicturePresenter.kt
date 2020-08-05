package moxy.sample.dailypicture.ui

import android.util.Log
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.PictureOfTheDay
import java.time.LocalDate

class DailyPicturePresenter(
    private val dailyPictureInteractor: DailyPictureInteractor
) : MvpPresenter<DailyPictureView>() {

    private var pictureOfTheDay: PictureOfTheDay? = null
    private var date: LocalDate? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadPicture()
    }

    fun onRefresh() {
        loadPicture()
    }

    fun onPictureClicked() {
        pictureOfTheDay?.let {
            // todo
        }
    }

    fun onRandomizeClicked() {
        date = dailyPictureInteractor.getRandomDate()
        loadPicture()
    }

    private fun loadPicture() {
        presenterScope.launch {
            viewState.showProgress(true)
            try {
                val picture = dailyPictureInteractor.getPicture(date)
                pictureOfTheDay = picture
                viewState.showPicture(picture)
            } catch (e: Exception) {
                Log.e("DailyPicturePresenter", "Could not load daily picture", e)
                viewState.showError("Something went wrong, please try again.")
            } finally {
                viewState.showProgress(false)
            }
        }
    }
}

package moxy.sample.dailypicture.ui

import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.sample.dailypicture.domain.DailyPictureInteractor

class DailyPicturePresenter(
    private val dailyPictureInteractor: DailyPictureInteractor
) : MvpPresenter<DailyPictureView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadPictureForToday()
    }

    private fun loadPictureForToday() {
        presenterScope.launch {
            viewState.showPicture(dailyPictureInteractor.getPictureForToday())
        }
    }
}

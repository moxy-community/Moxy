package moxy.sample.dailypicture.ui

import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.PictureOfTheDay
import moxy.sample.util.Logger
import java.time.LocalDate
import javax.inject.Inject

class DailyPicturePresenter
@Inject
constructor(
    private val dailyPictureInteractor: DailyPictureInteractor,
    private val logger: Logger
) : MvpPresenter<DailyPictureView>() {

    private var pictureOfTheDay: PictureOfTheDay? = null
    private var date: LocalDate? = null

    init {
        logger.d("DailyPicturePresenter", "init $this")
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        logger.d("DailyPicturePresenter", "onFirstViewAttach $this")
        loadPicture()
    }

    fun onRefresh() {
        loadPicture()
    }

    fun onPictureClicked() {
        pictureOfTheDay?.let {
            viewState.openBrowser(it.url)
        }
    }

    fun onRandomizeClicked() {
        date = dailyPictureInteractor.getRandomDate()
        loadPicture()
    }

    fun onOpenBrowserError() {
        viewState.showError("Browser app is not installed")
    }

    private fun loadPicture() {
        // Try rotating the screen, and look at the logs!
        // You'll see that the network request is not executed
        // the second time after the screen is rotated. Awesome!
        logger.d("DailyPicturePresenter", "Loading picture for date: $date")

        // We use the handy presenterScope extension from moxy-ktx artifact to launch a coroutine.
        // You can do your own implementation for asynchronous work: RxJava, plain old callbacks,
        // or something else. Just remember to call viewState methods only from Main thread!
        presenterScope.launch {
            viewState.showProgress(true)
            try {
                val picture = dailyPictureInteractor.getPicture(date)
                pictureOfTheDay = picture
                viewState.setTitle(picture.title)
                viewState.setDescription(picture.explanation)
                if (picture.copyright.isEmpty()) {
                    viewState.hideCopyright()
                } else {
                    viewState.showCopyright(picture.copyright)
                }
                when (picture.mediaType) {
                    PictureOfTheDay.MediaType.IMAGE -> viewState.showImage(picture.url)
                    PictureOfTheDay.MediaType.VIDEO -> viewState.showVideo()
                    PictureOfTheDay.MediaType.UNKNOWN -> viewState.hideImage()
                }
            } catch (e: Exception) {
                logger.e("DailyPicturePresenter", "Could not load daily picture", e)
                viewState.showError("Something went wrong, please try again.")
            } finally {
                viewState.showProgress(false)
            }
        }
    }
}

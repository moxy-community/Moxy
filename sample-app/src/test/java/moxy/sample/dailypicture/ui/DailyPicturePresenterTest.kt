package moxy.sample.dailypicture.ui

import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import moxy.sample.TestCoroutineDispatcherProvider
import moxy.sample.dailypicture.domain.DailyPictureInteractor
import moxy.sample.dailypicture.domain.PictureOfTheDay
import moxy.sample.util.ConsoleLogger
import org.junit.Test
import java.time.LocalDate

class DailyPicturePresenterTest : BaseTest() {

    private val url = "some_url"

    private val title = "some_title"

    private val description = "some_description"

    private val copyright = "some_copyright"

    private val interactor: DailyPictureInteractor = mock()

    @Test
    fun `on first view attach`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.IMAGE))
    }) {
        verify(view).showProgress(true)
        verify(view).showImage(url)
        verify(view).setTitle(title)
        verify(view).setDescription(description)
        verify(view).showCopyright(copyright)
        verify(view).showProgress(false)
    }

    @Test
    fun `load image success test`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.IMAGE))
    }) {
        clearInvocations(view)

        presenter.onRefresh()

        verify(view).showProgress(true)
        verify(view).showImage(url)
        verify(view).setTitle(title)
        verify(view).setDescription(description)
        verify(view).showCopyright(copyright)
        verify(view).showProgress(false)
    }

    @Test
    fun `load video success test`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.VIDEO))
    }) {
        clearInvocations(view)

        presenter.onRefresh()

        verify(view).showProgress(true)
        verify(view).showVideo()
        verify(view).setTitle(title)
        verify(view).setDescription(description)
        verify(view).showCopyright(copyright)
        verify(view).showProgress(false)
    }

    @Test
    fun `load unknown media type success test`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.UNKNOWN))
    }) {
        clearInvocations(view)

        presenter.onRefresh()

        verify(view).showProgress(true)
        verify(view).hideImage()
        verify(view).setTitle(title)
        verify(view).setDescription(description)
        verify(view).showCopyright(copyright)
        verify(view).showProgress(false)
    }

    @Test
    fun `load image error test`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenThrow(RuntimeException::class.java)
    }) {
        clearInvocations(view)

        presenter.onRefresh()

        verify(view).showProgress(true)
        verify(view).showError("Something went wrong, please try again.")
        verify(view).showProgress(false)
    }

    @Test
    fun `on picture clicked`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.IMAGE))
    }) {
        clearInvocations(view)

        presenter.onPictureClicked()

        verify(view).openBrowser(url)
    }

    @Test
    fun `on randomize clicked`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        val date = LocalDate.of(2020, 1, 1)
        whenever(interactor.getRandomDate()).thenReturn(date)
        whenever(interactor.getPicture(date)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.IMAGE))
    }) {
        clearInvocations(view)

        presenter.onRandomizeClicked()

        verify(view).showProgress(true)
        verify(view).showImage(url)
        verify(view).setTitle(title)
        verify(view).setDescription(description)
        verify(view).showCopyright(copyright)
        verify(view).showProgress(false)
    }

    @Test
    fun `on open browser error`() = createMockPresenterBlocking({
        DailyPicturePresenter(interactor, TestCoroutineDispatcherProvider(), ConsoleLogger())
    }, {
        whenever(interactor.getPicture(null)).thenReturn(PictureOfTheDay(LocalDate.now(), url, title, description, copyright, PictureOfTheDay.MediaType.IMAGE))
    }) {
        clearInvocations(view)

        presenter.onOpenBrowserError()

        verify(view).showError("Browser app is not installed")
    }
}
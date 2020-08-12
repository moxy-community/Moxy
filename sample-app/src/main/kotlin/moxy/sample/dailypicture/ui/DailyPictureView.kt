package moxy.sample.dailypicture.ui

import moxy.MvpView
import moxy.sample.dailypicture.domain.PictureOfTheDay
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

/**
 * A View interface for "Daily Picture" screen.
 *
 * It demonstrates the usage of two most common view method strategies:
 * - [AddToEndSingle] for state
 * - [OneExecution] for events
 *
 * Note that it extends [MvpView] and all methods return nothing.
 */
interface DailyPictureView : MvpView {

    /**
     * Show and astronomy picture of the day.
     *
     * This method is has [AddToEndSingle] strategy because
     * it represents a _state_ - the picture is displayed to the user until a
     * different picture is loaded, and it should still be displayed
     * after screen rotation.
     */
    @AddToEndSingle
    fun showImage(url: String)

    @AddToEndSingle
    fun showVideo()

    @AddToEndSingle
    fun hideImage()

    @AddToEndSingle
    fun showProgress(isProgress: Boolean)

    /**
     * Open web browser to show some URL content.
     *
     * This should probably be implemented with a separate Router entity
     * which is responsible for navigation, but for the sample purposes we go with
     * a lazy option.
     */
    @OneExecution
    fun openBrowser(url: String)

    /**
     * Show error message.
     *
     * This method has [OneExecution] strategy because
     * showing an error message is an _event_ - it must
     * happen only once, and should not happen again after
     * screen rotation.
     */
    @OneExecution
    fun showError(message: String)

    @AddToEndSingle
    fun setTitle(text: String)

    @AddToEndSingle
    fun setDescription(text: String)

    @AddToEndSingle
    fun showCopyright(text: String)

    @AddToEndSingle
    fun hideCopyright()
}

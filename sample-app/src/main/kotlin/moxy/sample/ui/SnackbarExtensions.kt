package moxy.sample.ui

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(
    message: String,
    @BaseTransientBottomBar.Duration length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, message, length).show()
}

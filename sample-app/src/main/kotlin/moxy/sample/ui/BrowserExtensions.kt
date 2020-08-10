package moxy.sample.ui

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

fun Fragment.openBrowser(uri: Uri, errorHandler: () -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    startActivityIfResolved(intent, errorHandler)
}

private fun Fragment.startActivityIfResolved(intent: Intent, errorHandler: () -> Unit) {
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(intent)
    } else {
        errorHandler.invoke()
    }
}

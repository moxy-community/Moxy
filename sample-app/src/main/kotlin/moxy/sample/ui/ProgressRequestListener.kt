package moxy.sample.ui

import coil.request.ImageRequest
import coil.request.ImageResult

class ProgressRequestListener(
    private val showProgress: (isProgress: Boolean) -> Unit
) : ImageRequest.Listener {

    override fun onStart(request: ImageRequest) {
        showProgress.invoke(true)
    }

    override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
        showProgress.invoke(false)
    }

    override fun onCancel(request: ImageRequest) {
        showProgress.invoke(false)
    }

    override fun onError(request: ImageRequest, throwable: Throwable) {
        showProgress.invoke(false)
    }
}

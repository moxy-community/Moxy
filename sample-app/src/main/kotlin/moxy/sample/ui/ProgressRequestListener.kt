package moxy.sample.ui

import coil.decode.DataSource
import coil.request.Request

class ProgressRequestListener(
    private val showProgress: (isProgress: Boolean) -> Unit
) : Request.Listener {

    override fun onStart(request: Request) {
        showProgress.invoke(true)
    }

    override fun onSuccess(request: Request, source: DataSource) {
        showProgress.invoke(false)
    }

    override fun onCancel(request: Request) {
        showProgress.invoke(false)
    }

    override fun onError(request: Request, throwable: Throwable) {
        showProgress.invoke(false)
    }
}

package moxy.sample.util

import java.lang.Exception

class AndroidLogger : Logger {

    override fun d(tag: String, msg: String) {
        android.util.Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String, exception: Exception) {
        android.util.Log.e(tag, msg, exception)
    }
}
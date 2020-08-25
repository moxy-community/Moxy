package moxy.sample.util

import android.util.Log
import java.lang.Exception
import javax.inject.Inject

class AndroidLogger @Inject constructor() : Logger {

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String, exception: Exception) {
        Log.e(tag, msg, exception)
    }
}
package example.com.androidxsample.view

import android.util.Log

class Logger {

    fun printErrorLog() {
        Log.e(MainActivity.TAG, "logger hash code : ${hashCode()}")
    }

}
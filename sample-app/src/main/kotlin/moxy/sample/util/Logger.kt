package moxy.sample.util

import java.lang.Exception

interface Logger {
    fun d(tag: String, msg: String)

    fun e(tag: String, msg: String, exception: Exception)
}
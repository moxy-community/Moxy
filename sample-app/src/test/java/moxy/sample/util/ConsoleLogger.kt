package moxy.sample.util

import java.lang.Exception

class ConsoleLogger : Logger {

    override fun d(tag: String, msg: String) {
        println("$tag $msg")
    }

    override fun e(tag: String, msg: String, exception: Exception) {
        println("$tag $msg")
    }
}
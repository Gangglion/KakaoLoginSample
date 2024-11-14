package com.example.kakaologinsample.util

import android.util.Log
import com.example.kakaologinsample.BuildConfig


object LogUtil {
    const val TAG = "shhan"
    private var isDev = BuildConfig.DEBUG

    fun v(msg: String, tr: Throwable? = null) {
        if(isDev) {
            Log.v(TAG, "💬 ${getLineNumber()}\n$msg", tr)
        }
    }

    fun d(msg: String, tr: Throwable? = null) {
        if(isDev) {
            Log.d(TAG, "\uD83D\uDC1E ${getLineNumber()}\n$msg", tr)
        }
    }

    fun i(msg: String, tr: Throwable? = null) {
        if(isDev) {
            Log.i(TAG, "ℹ\uFE0F ${getLineNumber()}\n$msg", tr)
        }
    }

    fun w(msg: String, tr: Throwable? = null) {
        if(isDev) {
            Log.w(TAG, "⚠\uFE0F ${getLineNumber()}\n$msg", tr)
        }
    }

    fun e(msg: String, tr: Throwable? = null) {
        if(isDev) {
            Log.e(TAG, "❌ ${getLineNumber()}\n$msg", tr)
        }
    }

    private fun getLineNumber(): String {
        val stackTrace = Thread.currentThread().stackTrace
        for (i in stackTrace.indices) {
            if (stackTrace[i].className != LogUtil::class.java.name) {
                if (i > 3) {
                    val fullClassName = stackTrace[i].className
                    val simpleClassName = fullClassName.substringAfterLast('.').substringBefore('$')
                    val lineNumber = stackTrace[i].lineNumber
                    return "$simpleClassName.kt:$lineNumber"
                }
            }
        }
        return "Unknown Class"
    }
}
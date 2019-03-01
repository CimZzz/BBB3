package com.gogoh5.apps.quanmaomao.library.utils

import android.util.Log
import com.gogoh5.apps.quanmaomao.library.toolkits.HunterManager

object LogUtils {
    var hunterManager: HunterManager? = null

    const val TYPE_CIMZZZ = 1
    const val TYPE_REQUEST = 2
}

fun logCimZzz(any: Any?) {
    Log.v("CimZzz", any.toString())
    LogUtils.hunterManager?.doEat(LogUtils.TYPE_CIMZZZ, "CimZzz", any.toString())
}

fun logRequest(any: Any?) {
    Log.v("QMMRequest", any.toString())
    LogUtils.hunterManager?.doEat(LogUtils.TYPE_REQUEST, "QMMRequest", any.toString())
}
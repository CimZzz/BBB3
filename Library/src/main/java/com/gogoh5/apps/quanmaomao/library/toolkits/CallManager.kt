package com.gogoh5.apps.quanmaomao.library.toolkits

import android.util.SparseArray
import okhttp3.Call

class CallManager {
    private val callMap: SparseArray<Call?> = SparseArray()

    fun stopCall(position: Int) {
        val call = callMap.get(position) ?: return

        if(!call.isCanceled)
            call.cancel()
    }

    fun addCall(position: Int, call: Call) {
        val oldCall = callMap.get(position)
        oldCall?.cancel()

        callMap.put(position, call)
    }

    fun removeCall(position: Int) {
        callMap.remove(position)
    }

    fun checkCallExist(position: Int): Boolean {
        val call = callMap.get(position)
        return call != null && !call.isCanceled
    }

    fun stopAll() {
        (0 until callMap.size()).forEach {
            val call = callMap.valueAt(it)?: return@forEach
            if(!call.isCanceled)
                call.cancel()
        }

        callMap.clear()
    }
}
package com.gogoh5.apps.quanmaomao.library.utils

import android.widget.Toast
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

object ToastUtils {
    private var toast: Toast? = null

    fun sendToast(any: Any?) {
        val oldToast = toast
        if(oldToast != null)
            oldToast.cancel()

        val toast = Toast.makeText(SysContext.getApp(), any.toString(), Toast.LENGTH_SHORT)
        toast.show()
        this.toast = toast
    }
}
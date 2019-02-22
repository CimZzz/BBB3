package com.gogoh5.apps.quanmaomao.library.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

object ViewUtils {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T: View> inflateView(parent: ViewGroup, layoutId: Int) : T = LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as T
    inline fun <reified T: View> inflateView(parent: View, layoutId: Int) : T? =
        if(parent is ViewGroup)
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as T
        else null
}
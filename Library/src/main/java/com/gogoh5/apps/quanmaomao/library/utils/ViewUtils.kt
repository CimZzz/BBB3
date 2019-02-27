package com.gogoh5.apps.quanmaomao.library.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.extensions.forEach

object ViewUtils {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T: View> inflateView(parent: ViewGroup, layoutId: Int) : T = LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as T
    inline fun <reified T: View> inflateView(parent: View, layoutId: Int) : T? =
        if(parent is ViewGroup)
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as T
        else null


    fun setViewEnable(view: View, isEnable: Boolean) {
        if(view is ViewGroup) {
            view.forEach {
                chilView->
                if (chilView is ViewGroup)
                    setViewEnable(chilView, isEnable)

                chilView.isEnabled = isEnable
            }
        }
    }
}
package com.gogoh5.apps.quanmaomao.library.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.Serializable
import java.lang.Exception

abstract class BaseLink: Serializable {
    protected var isFromInside: Boolean = true

    fun processIntent(intent: Intent, context: Context) {
        if(context !is Activity)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(BaseUI.MASK_FROM_INSIDE, isFromInside)
        intent.putExtra(BaseUI.MASK_LINK, this)
    }

    fun perform(context: Context) {
        try {
            doPerform(context)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract protected fun doPerform(context: Context)
}
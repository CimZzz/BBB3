package com.gogoh5.apps.quanmaomao.library.base

import android.content.Context

interface IBaseUIView: IView {
    fun getContext(): Context
    fun getPassLink(): BaseLink?
    fun showWaitDialog(txt: String)
    fun closeWaitDialog()
    fun closeDirectly()

}
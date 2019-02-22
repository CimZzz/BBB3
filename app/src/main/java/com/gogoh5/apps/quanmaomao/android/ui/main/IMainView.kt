package com.gogoh5.apps.quanmaomao.android.ui.main

import com.gogoh5.apps.quanmaomao.library.base.IBaseUIView

interface IMainView: IBaseUIView {


    fun backward()
    fun transferToPage(page: Int)
    fun refreshAll()
}
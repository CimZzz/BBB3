package com.gogoh5.apps.quanmaomao.android.ui.setting

import com.gogoh5.apps.quanmaomao.library.base.IBaseUIView

interface ISettingView: IBaseUIView {
    fun showWxAuth(isSuccess: Boolean)
    fun showCalculatingCache()
    fun showCache(byteCountStr: String?)
}
package com.gogoh5.apps.quanmaomao.android.ui.setting

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils

class SettingPresenter(view: ISettingView) : BaseMethodPresenter<ISettingView, SettingMethod>(view) {
    override fun generateMethod(): SettingMethod = SettingMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        view.showWxAuth(method.checkWxAuth())
        view.showCalculatingCache()
        method.calculateCache()
    }

    fun onCalculateCallback(byteCount: Long) {
        if(byteCount == 0L)
            view.showCache(null)
        else view.showCache(StringUtils.formatByteSize(byteCount))
    }


    fun doWxAuth() {
        if(!method.checkWxAuth())
            method.authWx()
        else {
            method.unAuthWx()
            view.showWxAuth(false)
            sendToast("取消授权成功")
        }
    }

    fun showWxAuthDialog() {
        view.showWaitDialog("授权中...")
    }

    fun onWxAuthSuccess() {
        view.showWxAuth(true)
        view.closeWaitDialog()
        sendToast("微信授权成功")
    }

    fun onWxAuthFailure(errorTxt: String? = null) {
        view.closeWaitDialog()
        sendToast(errorTxt?:"微信授权失败")
    }

    fun clearCache() {
        view.showWaitDialog("清除缓存中...")
        method.clearCache()
    }

    fun onClearCacheCallback() {
        sendToast("已清空缓存...")
        view.closeWaitDialog()
        view.showCache(null)
    }
}
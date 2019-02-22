package com.gogoh5.apps.quanmaomao.android.ui.setting

import android.view.View
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.ui_setting.*

class SettingUI: BaseUI<SettingPresenter>(), ISettingView {
    override fun initPresenter(): SettingPresenter = SettingPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_setting)

        backBtn.tapWith("设置返回按钮", ActionSource.SETTING_BACK) {
            closeDirectly()
        }

        wxAuthContainer.tapWith("设置微信登录", ActionSource.SETTING_WX_AUTH) {
            presenter.doWxAuth()
        }

        cacheContainer.tapWith("设置清除缓存", ActionSource.SETTING_CACHE) {
            presenter.clearCache()
        }

        quitContainer.tapWith("设置退出微信登录", ActionSource.SETTING_WX_QUIT) {
            presenter.doWxAuth()
        }
    }

    override fun showWxAuth(isSuccess: Boolean) {
        if(isSuccess) {
            wxAuthTxt.text = "微信已授权"
            wxAuthTipsTxt.text = "退出登录"
            quitContainer.visibility = View.VISIBLE
        }
        else {
            wxAuthTxt.text = "微信未授权"
            wxAuthTipsTxt.text = ""
            quitContainer.visibility = View.GONE
        }
    }

    override fun showCalculatingCache() {
        cacheTipsTxt.text = "计算中..."
    }

    override fun showCache(byteCountStr: String?) {
        cacheTipsTxt.text = byteCountStr?:""
    }
}
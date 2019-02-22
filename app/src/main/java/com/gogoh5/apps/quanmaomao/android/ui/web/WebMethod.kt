package com.gogoh5.apps.quanmaomao.android.ui.web

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.WebViewBridge

class WebMethod(presenter: WebPresenter) : BaseMethod<WebPresenter>(presenter) {
    fun getPid(): String? = SysContext.getSetting().initBean.pid

    fun getWebBridge(): WebViewBridge {
        val webViewBridge = WebViewBridge()
        webViewBridge.aliSuccessDataHunter = RefDataHunter(presenterRef) {
            presenter, params->
            presenter.onAliSuccess()
            SysContext.getBus().aliScAuth(true, params[0] as String?)
        }
        webViewBridge.aliFailureDataHunter = RefDataHunter(presenterRef) {
            presenter, params->
            presenter.onAliFailure()
            SysContext.getBus().aliScAuth(false, params[0] as String?)
        }

        return webViewBridge
    }


    fun sendAliAuthFailure() {
        SysContext.getBus().aliScAuth(false, "授权失败")
    }
}

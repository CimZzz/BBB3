package com.gogoh5.apps.quanmaomao.android.ui.web

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.links.WebLink

class WebPresenter(view: IWebView) :
    BaseMethodPresenter<IWebView, WebMethod>(view) {
    lateinit var link: WebLink
    var isAliAuth: Boolean = false
    var acceptAliAuthCallback: Boolean = false

    override fun generateMethod(): WebMethod =
        WebMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        val link = view.getPassLink()
        if(link == null || link !is WebLink) {
            view.closeDirectly()
            return
        }

        this.link = link
        this.isAliAuth = this.link.isAliAuth
        if(this.isAliAuth)
            view.lockTitle("淘宝授权")
        view.configWebBridge(method.getWebBridge())

        when(link.aliPage) {
            WebLink.PAGE_ORDER -> view.showAliOrder(method.getPid())
            else -> view.showUrl(link.url, link.useAliTrade, method.getPid())
        }
    }

    fun refresh() {
        view.refresh()
    }

    fun onAliSuccess() {
        acceptAliAuthCallback = true
        view.closeDirectly()
    }

    fun onAliFailure() {
        acceptAliAuthCallback = true
        view.closeDirectly()
    }

    fun checkAliAuth() {
        if(isAliAuth && !acceptAliAuthCallback)
            method.sendAliAuthFailure()
    }
}
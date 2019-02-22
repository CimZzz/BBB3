package com.gogoh5.apps.quanmaomao.android.ui.web

import com.gogoh5.apps.quanmaomao.library.base.IBaseUIView
import com.gogoh5.apps.quanmaomao.library.toolkits.WebViewBridge

interface IWebView: IBaseUIView {
    fun lockTitle(title: String)
    fun configWebBridge(webBridge: WebViewBridge)
    fun showAliOrder(pid: String?)
    fun showUrl(url: String, useAliTrade: Boolean, pid: String?)
    fun refresh()
}
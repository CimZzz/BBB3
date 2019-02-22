package com.gogoh5.apps.quanmaomao.library.extended.webview

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter


class CustomChromeClient() : WebChromeClient() {
    var titleHunter: RefDataHunter<*>? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        if(title != null)
            titleHunter?.eat(title)
    }
}
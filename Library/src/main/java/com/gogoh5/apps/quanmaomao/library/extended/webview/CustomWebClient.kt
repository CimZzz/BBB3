package com.gogoh5.apps.quanmaomao.library.extended.webview

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter

class CustomWebClient: WebViewClient() {
    var pageStartHunter: RefDataHunter<*>? = null
    var pageFinishHunter: RefDataHunter<*>? = null

    private var isFailure = false

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        isFailure = false
        if(url != null)
            pageStartHunter?.eat(url)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        isFailure = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if(url != null)
            pageFinishHunter?.eat(!isFailure, url)
    }
}
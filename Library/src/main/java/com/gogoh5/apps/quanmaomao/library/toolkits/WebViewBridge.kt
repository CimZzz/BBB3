package com.gogoh5.apps.quanmaomao.library.toolkits

import android.webkit.JavascriptInterface

class WebViewBridge {
    var aliSuccessDataHunter: RefDataHunter<*>? = null
    var aliFailureDataHunter: RefDataHunter<*>? = null

    @JavascriptInterface
    fun onAliSuccess(msg: String) {
        aliSuccessDataHunter?.eat(msg)
    }

    @JavascriptInterface
    fun onAliFailure(msg: String) {
        aliFailureDataHunter?.eat(msg)
    }
}
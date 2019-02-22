package com.gogoh5.apps.quanmaomao.android.ui.web

import android.annotation.SuppressLint
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage
import com.alibaba.baichuan.android.trade.page.AlibcPage
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extended.webview.CustomChromeClient
import com.gogoh5.apps.quanmaomao.library.extended.webview.CustomWebClient
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.toolkits.DataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.WebViewBridge
import kotlinx.android.synthetic.main.ui_web.*
import kotlinx.android.synthetic.main.ui_web.view.*

class WebUI: BaseUI<WebPresenter>(),
    IWebView {
    val chromeClient = CustomChromeClient()
    val webClient = CustomWebClient()

    var isLockTitle: Boolean = false

    override fun initPresenter(): WebPresenter =
        WebPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_web)

        backBtn.tapWith("H5返回按钮", ActionSource.WEB_BACK) {
            backward()
        }

        closeBtn.tapWith("H5关闭按钮", ActionSource.WEB_CLOSE) {
            closeDirectly()
        }

        viewHandler.viewStubFirstBind = {
            view, id ->
            when(id) {
                R.id.error -> {
                    view.tapWith("H5失败刷新", ActionSource.WEB_ERROR) {
                        presenter.refresh()
                    }
                }
            }
        }

        val webView = viewHandler.content
        SysContext.configWebView(webView)

        chromeClient.titleHunter = DataHunter(this) {
            page, params->
            if(!page.isLockTitle)
                page.titleTxt.text = params[0] as String

        }

        webClient.pageStartHunter = DataHunter(this) {
            page, _->
            page.showLoading()
            if(!page.isLockTitle)
                page.titleTxt.text = "加载中...."
        }

        webClient.pageFinishHunter = DataHunter(this) {
            page, params->
            if(params[0] as Boolean)
                page.showWebView()
            else page.showError()
        }

        webView.webViewClient = webClient
        webView.webChromeClient = chromeClient
    }

    override fun onBackPressed() {
        backward()
    }


    override fun finish() {
        super.finish()
        presenter.checkAliAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        AlibcTradeSDK.destory()
        viewHandler.content.destroy()
    }

    override fun lockTitle(title: String) {
        isLockTitle = true
        titleTxt.text = title
    }

    @SuppressLint("AddJavascriptInterface")
    override fun configWebBridge(webBridge: WebViewBridge) {
        viewHandler.content.addJavascriptInterface(webBridge,"mmmInterface")
    }

    override fun showAliOrder(pid: String?) {
        val taokeParams = AlibcTaokeParams()
        taokeParams.pid = pid
        AlibcTrade.show(this, viewHandler.content, webClient, chromeClient, AlibcMyOrdersPage(0, false), AlibcShowParams(OpenType.H5, false), taokeParams, null, object: AlibcTradeCallback {
            override fun onFailure(p0: Int, p1: String?) {

            }

            override fun onTradeSuccess(p0: AlibcTradeResult?) {
            }
        })
    }

    override fun showUrl(url: String, useAliTrade: Boolean, pid: String?) {
        if(useAliTrade) {
            val taokeParams = AlibcTaokeParams()
            taokeParams.pid = pid
            AlibcTrade.show(this, viewHandler.content, webClient, chromeClient, AlibcPage(url), AlibcShowParams(OpenType.H5, false), taokeParams, null, object: AlibcTradeCallback {
                override fun onFailure(p0: Int, p1: String?) {

                }

                override fun onTradeSuccess(p0: AlibcTradeResult?) {
                }
            })
        }
        else viewHandler.content.loadUrl(url)
    }


    override fun refresh() {
        viewHandler.content.reload()
    }

    private fun showError() {
        viewHandler.showView(R.id.error)
    }

    private fun showLoading() {
        viewHandler.showView(R.id.loading)
    }

    private fun showWebView() {
        viewHandler.showView(R.id.content)
    }


    private fun backward() {
        if(!isLockTitle && viewHandler.content.canGoBack())
            viewHandler.content.goBack()
        else closeDirectly()
    }
}
package com.gogoh5.apps.quanmaomao.android.ui.main.cart

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.ui.main.BaseMainPage
import com.gogoh5.apps.quanmaomao.android.ui.main.MainPresenter
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extended.webview.CustomChromeClient
import com.gogoh5.apps.quanmaomao.library.extended.webview.CustomWebClient
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.toolkits.DataHunter
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.page_cart.view.*

class CartPage(mainPresenter: MainPresenter) : BaseMainPage<Unit>(mainPresenter) {
    val chromeClient = CustomChromeClient()
    val webClient = CustomWebClient()
    var webView: WebView? = null

    override fun generateDataParcelable() {
    }
    override fun isViewFromObject(view: View): Boolean = rootView == view

    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.page_cart)

    override fun initViewPage(parent: ViewGroup, bindBean: Any, position: Int) {
        super.initViewPage(parent, bindBean, position)

        rootView.topGuide.setSize(height = SysContext.getStatusBarHeight())

        rootView.backBtn.tapWith("购物车返回按钮", ActionSource.MAIN_SEARCH_SEARCH) {

        }

        rootView.viewHandler.viewStubFirstBind = {
            view, id->
            when(id) {
                R.id.error -> {
                    view.tapWith {
                        loadCartPage()
                    }
                }
            }
        }

        webClient.pageStartHunter = DataHunter(this) {
            page, _->
            page.showLoading()
        }

        webClient.pageFinishHunter = DataHunter(this) {
            page, params->
            if(params[0] as Boolean)
                page.showWebView()
            else page.showError()
        }

        webView = rootView.viewHandler.content
        SysContext.configWebView(webView)
        webView?.webChromeClient = chromeClient
        webView?.webViewClient = webClient
    }

    override fun attachView(parent: ViewGroup) {
        parent.addView(rootView)
    }

    override fun detachView(parent: ViewGroup) {
        parent.removeView(rootView)
    }

    override fun onEnterPage() {
        if(isFirstEnter)
            loadCartPage()
    }

    override fun onQuitPage() {

    }

    override fun onDestroyPage() {
        webView?.destroy()
        AlibcTradeSDK.destory()
    }

    private fun loadCartPage() {
        AlibcTrade.show(mainPresenter.getActivity(), webView, webClient, chromeClient, AlibcMyCartsPage(), AlibcShowParams(OpenType.H5, false), AlibcTaokeParams(), null, object : AlibcTradeCallback {
            override fun onFailure(p0: Int, p1: String?) {

            }

            override fun onTradeSuccess(p0: AlibcTradeResult?) {

            }
        })
    }

    private fun showWebView() {
        rootView.viewHandler.showView(R.id.content)
    }

    private fun showError() {
        rootView.viewHandler.showView(R.id.error)
    }

    private fun showLoading() {
        rootView.viewHandler.showView(R.id.loading)
    }
}
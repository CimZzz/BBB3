package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import android.view.View
import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.databeans.AliScAuthBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductDetailBean
import com.gogoh5.apps.quanmaomao.library.entities.links.ProductLink
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class ProductDetailPresenter(view: IProductDetailView) :
    BaseMethodPresenter<IProductDetailView, ProductDetailMethod>(view) {
    lateinit var link: ProductLink

    var isNeedBuy: Boolean = false
    var aliScAuthBean: AliScAuthBean? = null

    var convertLink: String? = null
    var detailBean: ProductDetailBean? = null

    override fun generateMethod(): ProductDetailMethod = ProductDetailMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        val passLink = view.getPassLink()
        if(passLink !is ProductLink) {
            view.closeDirectly()
            return
        }

        link = passLink

        requestAliScStatus()
        requestConvertLink()
        requestProductDetail()
    }


    fun requestProductDetail() {
        view.showLoading()
        method.requestProductDetail(link.productId)
    }


    override fun onLazyLoad() {
        val detailLazyLoadBean = getLazyLoadParams(ProductDetailUI.LAZY_LOAD_DETAIL)
        if(detailLazyLoadBean != null) {
            if(detailLazyLoadBean.isSuccess()) {
                val detailBean = detailLazyLoadBean.obj as ProductDetailBean
                this.detailBean = detailBean
                view.showContent(detailBean)
            }
            else view.showError()
        }
    }

    fun requestAliScStatus() {
        method.requestAliScStatus()
    }

    fun onAliScStatusSuccess(aliScAuthBean: AliScAuthBean) {
        this.aliScAuthBean = aliScAuthBean
        if(isNeedBuy) {
            if(viewEnter)
                view.closeWaitDialog()
            else buy()
        }
    }

    fun onAliScStatusFailure(errorTxt: String) {
        if(isNeedBuy) {
            isNeedBuy = false
            view.closeWaitDialog()
            sendToast(errorTxt)
        }
    }

    fun onAliScAuthCallback(isSuccess: Boolean, msg: String?) {
        if(isSuccess) {
            aliScAuthBean?.grant(method.getAliUserId())
            buy()
        }
        else {
            if(!msg.isNullOrBlank())
                sendToast(msg)
            view.closeWaitDialog()
        }
    }

    fun requestConvertLink() {
        method.requestConvertLink(link.productId)
    }

    fun onConvertLinkCallback(linkStr: String?) {
        convertLink = linkStr?:""
        if(isNeedBuy)
            buy()
    }


    fun buy() {
        val authBean = aliScAuthBean
        if(authBean == null) {
            isNeedBuy = true
            view.showWaitDialog("请稍后...")
            requestAliScStatus()
            return
        }

        if(!authBean.isGrant(method.getAliUserId())) {
            if(!isNeedBuy) {
                isNeedBuy = true
                view.showWaitDialog("请稍后...")
            }

//            LinkUtils.linkWeb(authBean.getWebAuthUrl(), aliPage = WebLink.PAGE_ORDER, context = view.getContext())
            LinkUtils.linkWeb(authBean.getWebAuthUrl(), isAliAuth = true, context = view.getContext())
            return
        }
        if(convertLink == null) {
            if(!isNeedBuy) {
                isNeedBuy = true
                view.showWaitDialog("请稍后...")
            }
            requestConvertLink()
            return
        }

        if(isNeedBuy) {
            isNeedBuy = false
            view.closeWaitDialog()
        }

        if(!convertLink.isNullOrEmpty())
            view.openBuyPage(convertLink, link.productId, method.getPid())
        else if(detailBean!!.isExistCoupon() && !detailBean!!.aliClick.isNullOrBlank())
            view.openBuyPage(detailBean!!.aliClick, link.productId, method.getPid())

        else view.openBuyPage(null, link.productId, method.getPid())

    }

    fun linkHome() {
        LinkUtils.linkMain(transferPage = 0)
        view.closeDirectly()
    }

    fun linkCart() {
        LinkUtils.linkMain(transferPage = 2)
        view.closeDirectly()
    }

    fun linkPreview(url: String, view: View) {
        LinkUtils.linkPreview(url, view, context = view.context)
    }
}
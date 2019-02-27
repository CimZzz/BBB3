package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.entities.databeans.AliScAuthBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductDetailBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.BusModule
import com.gogoh5.apps.quanmaomao.library.requests.GetAliScAuthStatusRequest
import com.gogoh5.apps.quanmaomao.library.requests.GetProductDetailRequest
import com.gogoh5.apps.quanmaomao.library.requests.GetProductLinkRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils

class ProductDetailMethod(productDetailPresenter: ProductDetailPresenter) : BaseMethod<ProductDetailPresenter>(productDetailPresenter) {
    init {
        SysContext.getBus().hunterManager.register("ProductDetailMethod", RefDataHunter(presenterRef) {
            presenter, params ->
            when(params[0]) {
                BusModule.TYPE_ALI_SC_AUTH -> {
                    presenter.onAliScAuthCallback(params[1] as Boolean, StringUtils.convertToNull(params[2] as String))
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SysContext.getBus().hunterManager.unregister("ProductDetailMethod")
    }

    fun requestProductDetail(productId: String) {
        appendRequest(0, GetProductDetailRequest(SysContext.getUser().uid, productId)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params->
                if(params[0] as Boolean)
                    presenter.putLazyLoadParams(ProductDetailUI.LAZY_LOAD_DETAIL,
                        BaseLazyLoadBean(params[1] as ProductDetailBean).buildSuccess())
                else presenter.putLazyLoadParams(ProductDetailUI.LAZY_LOAD_DETAIL,
                    BaseLazyLoadBean().buildFailure())
            })
        )
    }

    fun requestConvertLink(productId: String) {
        appendRequest(1, GetProductLinkRequest(SysContext.getUser().uid, productId)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params->
                if(params[0] as Boolean)
                    presenter.onConvertLinkCallback(params[1] as String)
                else presenter.onConvertLinkCallback(null)
            })
        )
    }

    fun requestAliScStatus() {
        appendRequest(2, GetAliScAuthStatusRequest(SysContext.getUser().uid)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params->
                if(params[0] as Boolean)
                    presenter.onAliScStatusSuccess(params[1] as AliScAuthBean)
                else presenter.onAliScStatusFailure(params[1] as String)
            })
        )
    }


    fun getPid(): String = SysContext.getSetting().initBean.pid
    fun getAliUserId(): String? = AlibcLogin.getInstance().session.userid
}
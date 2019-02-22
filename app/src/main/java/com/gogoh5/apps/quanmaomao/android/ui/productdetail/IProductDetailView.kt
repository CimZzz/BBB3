package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import com.gogoh5.apps.quanmaomao.library.base.IBaseUIView
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductDetailBean

interface IProductDetailView: IBaseUIView {
    fun showLoading()
    fun showError()
    fun showContent(detailBean: ProductDetailBean)
    fun openBuyPage(convertLink: String?, productId: String, pid: String?)
}
package com.gogoh5.apps.quanmaomao.android.entities.renderers

import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.SourceType

data class ProductRenderer(
    val GoodsID: String,
    val Pic: String,
    val sourceType: Int,
    val Title: String,
    val reward: Double,
    val Price: Double,
    val Quan_price: Double,
    val Sales_num: Long
): BaseRenderer()

fun parseProductRenderer(productBean: ProductBean?): ProductRenderer? {
    if(productBean == null)
        return null

    return ProductRenderer(
        productBean.GoodsID,
        productBean.Pic,
        if(productBean.IsTmall) SourceType.TMALL else SourceType.TAOBAO,
        productBean.Title,
        productBean.reward,
        productBean.Price,
        productBean.Quan_price,
        productBean.Sales_num
    )
}
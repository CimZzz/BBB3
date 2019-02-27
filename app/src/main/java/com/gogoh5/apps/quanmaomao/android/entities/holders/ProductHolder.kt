package com.gogoh5.apps.quanmaomao.android.entities.holders

import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductRenderer
import com.gogoh5.apps.quanmaomao.library.entities.links.ProductLink
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.SourceType
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseContentHolder
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import kotlinx.android.synthetic.main.item_product_renderer.view.*

class ProductHolder(parent: ViewGroup) : ListPageBaseContentHolder<ProductRenderer>(parent, R.layout.item_product_renderer) {

    init {
        itemView?.tapWith {
            callback.onClick(itemViewType, ProductLink(bean.GoodsID))
        }
    }

    override fun bindBean() {
        itemView.titleTxt.text = bean.Title
        itemView.priceTxt.text = StringUtils.buildBigPrice(bean.afterRewardPrice, SysContext.getResourceSp(R.dimen.text_bigPrice).toInt())
        itemView.saleNumTxt.text = "已抢${StringUtils.formatCount(bean.Sales_num)}件"
        itemView.rewardTxt.text = "返${StringUtils.formatPrice(bean.reward)}"

        if(bean.Quan_price != 0.0) {
            itemView.couponTxt.visibility = View.VISIBLE
            itemView.couponTxt.text = "${StringUtils.formatPrice(bean.Quan_price)}元券"
        }
        else itemView.couponTxt.visibility = View.GONE


        if(bean.IsTmall) {
            itemView.brandTxt.visibility = View.VISIBLE
            itemView.brandTxt.text = "天猫"
            itemView.brandTxt.setTextColor(SysContext.getColor(R.color.redColor))
            itemView.brandTxt.setBackgroundResource(R.drawable.bg_brand_tmall)
        }
        else {
            itemView.brandTxt.visibility = View.VISIBLE
            itemView.brandTxt.text = "淘宝"
            itemView.brandTxt.setTextColor(SysContext.getColor(R.color.whiteColor))
            itemView.brandTxt.setBackgroundResource(R.drawable.bg_brand_taobao)
        }

//        when(bean.sourceType) {
//            SourceType.TAOBAO -> {
//            }
//            SourceType.TMALL -> {
//            }
//            else -> itemView.brandTxt.visibility = View.GONE
//        }
    }

    override fun slowlyShow() {
        super.slowlyShow()
        SysContext.getGlide().loadNetPicDirectly(bean.Pic, itemView.productPicImg)
    }

    override fun attachToList() {
        if(isSlowlyShow)
            SysContext.getGlide().loadNetPicDirectly(bean.Pic, itemView.productPicImg)
    }

    override fun detachFromList() {
        SysContext.getGlide().cancel(itemView.productPicImg)
    }
}
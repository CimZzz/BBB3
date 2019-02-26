package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.utils.JSONUtils
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import java.io.Serializable
import java.util.*

data class ProductDetailBean (
    val bannerArr: Array<String>,
    val sourceType: Int,
    val IsTmall: Boolean,
    val title: String,
    val orgPrice: Double,
    val saleCount: Long,
    val price: Double,
    val priceTip: String,
    val afterRewardPrice: Double,
    val rewardPrice: Double,
    val couponPrice: Double,
    val couponTime: Long,
    val couponTimeStr: String,
    val shopIcon: String,
    val shopTitle: String,
    val shopId: String?,
    val goodDescScore: String,
    val sellerScore: String,
    val postScore: String,
    val aliClick: String?,
    val descDetail: Array<String>?
): Serializable {
    fun isExistReward(): Boolean = rewardPrice != 0.0
    fun isExistCoupon(): Boolean = couponPrice != 0.0 && couponTime != 0L
    fun isExistShop(): Boolean = !shopId.isNullOrEmpty()
    fun isExistDetail(): Boolean = !descDetail.isNullOrEmpty()
}

fun parseProductDetailBean(obj: JSONObject?): ProductDetailBean? {
    if(obj == null)
        return null

    val price = obj.getDouble("Price")?:return null
    val rewardPrice = obj.getDoubleValue("reward")
    val afterRewardPrice = price - rewardPrice

    val productPic = obj.getString("Pic")

    val bannerArr = JSONUtils.convertJSONArrayToStringArray(obj.getJSONArray("pics"))
    if(bannerArr == null && productPic.isNullOrBlank())
        return null

    val couponPrice = obj.getDoubleValue("Quan_price")
    val couponTime = obj.getLong("Quan_time")?: 0L
    val couponTimeStr = if(couponTime == 0L) "" else StringUtils.formatDate(couponTime, StringUtils.YMD2HMSDateFormat)

    var shopTitle = ""
    var shopIcon = ""
    var goodDescScore = ""
    var sellerScore = ""
    var postScore = ""
    val shopId: String? = obj.getJSONObject("shopDetail")?.let {
        val id = it.getString("shopId")?:return@let null
        shopTitle = it.getString("shopName")?:""
        shopIcon = it.getString("shopIcon")?:""
        it.getJSONObject("shopScore")?.let {
            shopScoreObj->
            goodDescScore = shopScoreObj.getString("item_score")?:""
            sellerScore = shopScoreObj.getString("service_score")?:""
            postScore = shopScoreObj.getString("delivery_score")?:""
        }
        id
    }

    var priceTip = "现价"
    if(rewardPrice != 0.0)
        priceTip = "返后"
    else if(couponPrice != 0.0)
        priceTip = "券后"

    val arr = JSONUtils.convertJSONArrayToStringArray(obj.getJSONArray("descDetail"))
    val list = LinkedList<String>()
    arr?.forEach {
        if(it.startsWith("http"))
            list.add(it)
        else list.add("https:$it")
    }

    return ProductDetailBean(
        bannerArr = bannerArr?:return null,
        sourceType = obj.getIntValue("sourceType"),
        IsTmall = obj.getIntValue("IsTmall") == 1,
        title = obj.getString("Title")?:return null,
        orgPrice = obj.getDoubleValue("Org_Price"),
        saleCount = obj.getLongValue("Sales_num"),
        price = price,
        priceTip = priceTip,
        afterRewardPrice = afterRewardPrice,
        rewardPrice = rewardPrice,
        couponPrice = couponPrice,
        couponTime = couponTime,
        couponTimeStr = couponTimeStr,
        shopIcon = shopIcon,
        shopTitle = shopTitle,
        shopId = shopId,
        goodDescScore = goodDescScore,
        sellerScore = sellerScore,
        postScore = postScore,
        aliClick = obj.getString("ali_click"),
        descDetail = list.toTypedArray()
    )

}
package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import java.io.Serializable

data class ProductRenderer(
    val GoodsID: String,
    val Title: String,
    val D_title: String,
    val Pic: String,
    val sourceType: Int,
    val Org_Price: Double = 0.0,
    val Price: Double = 0.0,
    val Quan_price: Double = 0.0,
    val Quan_time: Long = 0L,
    val Quan_link: String,
    val Sales_num: Long = 0,
    val Cid: String,
    val CommissionNum: Double = 0.0,
    val sImageUrl: String,
    val imageUrl: String,
    val ratio: Double,
    val shopId_s: String,
    val shopName_s: String,
    val shopIcon_s: String,
    val reward: Double = 0.0,
    val afterRewardPrice: Double = 0.0,
    val IsTmall: Boolean
): BaseRenderer() {

}

fun parseProductRenderer(obj: JSONObject?): ProductRenderer? {
    if(obj == null)
        return null

    val price = obj.getDouble("Price")?:return null
    val rewardPrice = obj.getDoubleValue("reward")
    val afterRewardPrice = price - rewardPrice


    return ProductRenderer(
        GoodsID = obj.getString("GoodsID")?:return null,
        Title = obj.getString("Title")?:return null,
        D_title = obj.getString("D_title")?:return null,
        Pic = obj.getString("Pic")?:return null,
        sourceType = obj.getInteger("sourceType")?:return null,
        Org_Price = obj.getDoubleValue("Org_Price"),
        Price = price,
        Quan_price = obj.getDoubleValue("Quan_price"),
        Quan_time = obj.getLongValue("Quan_time"),
        Quan_link = obj.getString("Quan_link")?:"",
        Sales_num = obj.getLongValue("Sales_num"),
        Cid = obj.getString("Cid")?:"",
        CommissionNum = obj.getDoubleValue("CommissionNum"),
        sImageUrl = obj.getString("sImageUrl")?:"",
        imageUrl = obj.getString("imageUrl")?:"",
        ratio = obj.getDoubleValue("ratio"),
        shopId_s = obj.getString("shopId_s")?:"",
        shopName_s = obj.getString("shopName_s")?:"",
        shopIcon_s = obj.getString("shopIcon_s")?:"",
        reward = rewardPrice,
        afterRewardPrice = afterRewardPrice,
        IsTmall = obj.getIntValue("IsTmall") == 1
    )
}
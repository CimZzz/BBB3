package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class MeBean (
    val uid: String,
    val totalSettle: Double,
    val estimate: Double,
    var balance: Double,
    var totalWithDraw: Double,
    val orderCount: Int
): Serializable

fun parseMeBean(uid: String, obj: JSONObject?): MeBean? {
    if(obj == null)
        return null

    return MeBean(
        uid,
        obj.getDoubleValue("totalSettle"),
        obj.getDoubleValue("estimate"),
        obj.getDoubleValue("balance"),
        obj.getDoubleValue("totalWithDraw"),
        obj.getIntValue("orderCount")
    )
}
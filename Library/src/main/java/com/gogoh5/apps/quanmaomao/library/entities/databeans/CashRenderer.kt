package com.gogoh5.apps.quanmaomao.library.entities.databeans

import android.graphics.Color
import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import java.lang.Exception

data class CashRenderer (
    val createTime: String,
    val status: String,
    val failReason: String,
    val applyNum: Double,
    val color: Int,
    val title: String
): BaseRenderer() {

}

fun parseCashRenderer(obj: JSONObject?): CashRenderer? {
    if(obj == null)
        return null


    val color = obj.getString("color")?:return null
    val colorInt: Int
    try {
        colorInt = Color.parseColor(color)
    }
    catch (e: Exception) {
        return null
    }

    return CashRenderer(
        createTime = obj.getString("createTime") ?: return null,
        status = obj.getString("status") ?: return null,
        applyNum = obj.getDouble("applyNum") ?: return null,
        failReason = obj.getString("failReason") ?: return null,
        color = colorInt,
        title = obj.getString("title")?:return null
    )
}
package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer

data class BalanceRenderer (
    val createTime: String,
    val balanceType: String,
    val changeNum: Double,
    val balanceBefore: Double,
    val balanceAfter: Double
): BaseRenderer() {

}

fun parseBalanceRenderer(obj: JSONObject?): BalanceRenderer? {
    if(obj == null)
        return null

    return BalanceRenderer(
        createTime = obj.getString("createTime") ?: return null,
        balanceType = obj.getString("balanceType") ?: return null,
        changeNum = obj.getDouble("changeNum") ?: return null,
        balanceBefore = obj.getDouble("balanceBefore") ?: return null,
        balanceAfter = obj.getDouble("balanceAfter") ?: return null
    )
}
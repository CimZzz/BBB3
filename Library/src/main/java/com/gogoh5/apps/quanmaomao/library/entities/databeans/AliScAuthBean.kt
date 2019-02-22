package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class AliScAuthBean (
    val taoAuthSpecialUrl: String,
    val taoAuthBothUrl: String,
    val taoAuthRelationUrl: String,
    var taoAuthStatus: Int,
    val taoAuthMode: Int,
    var userId: String?
): Serializable {
    fun isGrant(userId: String?): Boolean = !userId.isNullOrBlank() && userId == this.userId && taoAuthStatus == 3
    fun grant(userId: String?) {
        this.userId = userId
        this.taoAuthStatus = 3
    }

    fun isWebAuthMode(): Boolean = taoAuthMode == 1

    fun getWebAuthUrl(): String =
        when(taoAuthStatus) {
            1 -> taoAuthSpecialUrl
            2 -> taoAuthRelationUrl
            else -> taoAuthBothUrl
        }

}

fun parseAliScAuthBean(obj: JSONObject?): AliScAuthBean? {
    if(obj == null)
        return null


    return AliScAuthBean(
        obj.getString("taoAuthSpecialUrl")?:return null,
        obj.getString("taoAuthBothUrl")?:return null,
        obj.getString("taoAuthRelationUrl")?:return null,
        obj.getInteger("taoAuthStatus")?:return null,
        obj.getInteger("taoAuthMode")?:return null,
        obj.getString("userId")
    )
}
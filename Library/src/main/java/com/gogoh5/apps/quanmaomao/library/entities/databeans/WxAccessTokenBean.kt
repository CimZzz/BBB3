package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class WxAccessTokenBean (
    val unionId: String,
    val accessToken: String,
    val openid: String
): Serializable

fun parseWxAccessTokenBean(obj: JSONObject?): WxAccessTokenBean? {
    if(obj == null)
        return null

    return WxAccessTokenBean(
        obj.getString("unionid")?:return null,
        obj.getString("access_token")?:return null,
        obj.getString("openid")?:return null
    )
}
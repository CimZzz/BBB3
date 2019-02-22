package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class WxUserInfoBean (
    val unionId: String,
    val openId: String,
    val country: String?,
    val province: String?,
    val city: String?,
    val sex: String?,
    val nickname: String?,
    val headimgurl: String?,
    val privilege: JSONArray?
): Serializable

fun parseWxUserInfoBean(obj: JSONObject?): WxUserInfoBean? {
    if(obj == null)
        return null

    return WxUserInfoBean(
        unionId = obj.getString("unionid")?:return null,
        openId = obj.getString("openid")?:return null,
        country = obj.getString("country"),
        province = obj.getString("province"),
        city = obj.getString("city"),
        sex = obj.getString("sex"),
        nickname = obj.getString("nickname"),
        headimgurl = obj.getString("headimgurl"),
        privilege = obj.getJSONArray("privilege")
    )
}
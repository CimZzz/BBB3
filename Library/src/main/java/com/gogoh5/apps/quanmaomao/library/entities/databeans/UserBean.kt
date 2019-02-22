package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class UserBean (
    val uid: String,
    val name: String?,
    val sex: Int,
    val avatar: String?,
    val sid: String?
): Serializable

fun parseUserBean(obj: JSONObject?): UserBean? {
    if(obj == null)
        return null

    return UserBean(
        obj.getString("uid")?:return null,
        obj.getString("name"),
        obj.getIntValue("sex"),
        obj.getString("avatar"),
        obj.getString("sid")
    )
}
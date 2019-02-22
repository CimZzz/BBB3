package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.utils.JSONUtils

data class InitBean(
    val hotword: List<String>?,
    val pid: String
) {
    companion object {
        val EMPTY = InitBean(
            hotword = null,
            pid = ""
        )
    }
}

fun parseInitBean(obj: JSONObject?): InitBean? {
    if(obj == null)
        return null

    return InitBean(
        JSONUtils.convertJSONArrayToStringList(obj.getJSONArray("hotWord")),
        pid = obj.getString("pid")
    )
}
package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.alibaba.fastjson.JSONObject
import java.io.Serializable

data class AliCashBean (
    val price: Double,
    val name: String,
    val aliAccount: String,
    val mobile: String,
    val status: String?
): Serializable {

}

fun parseAliCashBean(obj: JSONObject?): AliCashBean? {
    if(obj == null)
        return null

    return AliCashBean(
        price = obj.getDouble("applyNum")?:return null,
        name = obj.getString("realName")?:return null,
        aliAccount = obj.getString("zhifubaoAccount")?:return null,
        mobile = obj.getString("mobile")?:"",
        status = obj.getString("status")
    )
}
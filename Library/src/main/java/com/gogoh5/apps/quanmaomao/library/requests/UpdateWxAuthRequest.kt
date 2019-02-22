package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseUserBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class UpdateWxAuthRequest(
    val uid: String,
    val unionId: String,
    val openId: String? = null,
    val nickname: String? = null,
    val sex: String? = null,
    val province: String? = null,
    val city: String? = null,
    val country: String? = null,
    val headImgUrl: String? = null,
    val privilege: JSONArray? = null
): BaseRequest<JSONObject>(Http.Update.WX_AUTH) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("uploadWxAuth") {
                it["uid"] = uid
                it["unionId"] = unionId
                it["openId"] = openId
                it["nickname"] = nickname
                it["sex"] = sex
                it["province"] = province
                it["city"] = city
                it["country"] = country
                it["headImgUrl"] = headImgUrl
                it["privilege"] = privilege
            }))

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response, true)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0)
            return parseUserBean(result.getJSONObject("data"))
        else configErrorTxt(result.getString("msg"))

        return null
    }
}
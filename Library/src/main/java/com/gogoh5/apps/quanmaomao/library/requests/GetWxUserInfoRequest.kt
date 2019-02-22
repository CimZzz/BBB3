package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseWxUserInfoBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildUrl
import okhttp3.Request
import okhttp3.Response

class GetWxUserInfoRequest(
    val accessToken: String,
    val openId: String
): BaseRequest<JSONObject>(Http.Get.WX_USER_INFO) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(buildUrl(url) {
                it["access_token"] = accessToken
                it["openid"] = openId
            })

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null || result.containsKey("errcode"))
            return null

        return parseWxUserInfoBean(result)
    }
}
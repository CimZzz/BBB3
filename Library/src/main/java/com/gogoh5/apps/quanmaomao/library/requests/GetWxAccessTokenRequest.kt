package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseWxAccessTokenBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildUrl
import okhttp3.Request
import okhttp3.Response

class GetWxAccessTokenRequest(
    val code: String
): BaseRequest<JSONObject>(Http.Get.WX_ACCESS_TOKEN) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(buildUrl(url) {
                it["appid"] = Constants.WX_APP_ID
                it["secret"] = Constants.WX_APP_SECRET
                it["code"] = code
                it["grant_type"] = "authorization_code"
            })

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null || result.containsKey("errcode"))
            return null

        return parseWxAccessTokenBean(result)
    }
}
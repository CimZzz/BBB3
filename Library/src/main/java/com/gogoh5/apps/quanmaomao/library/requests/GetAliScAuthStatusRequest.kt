package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseAliScAuthBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class GetAliScAuthStatusRequest(
    val uid: String
): BaseRequest<JSONObject>(Http.Get.ALI_SC_AUTH_STATUS) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("getScAuthStatus") {
                it["uid"] = uid
            }))

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response, true)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0)
            return parseAliScAuthBean(result.getJSONObject("data"))
        else configErrorTxt(result.getString("msg"))

        return null
    }
}
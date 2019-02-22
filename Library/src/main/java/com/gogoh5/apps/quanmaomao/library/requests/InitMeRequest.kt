package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseInitBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseMeBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApi
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class InitMeRequest(
    val uid: String
): BaseRequest<JSONObject>(Http.Init.ME) {
    override fun buildRequest(): Request.Builder {
        return Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("summary") {
                it["uid"] = uid
            }))
    }

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response, true)




    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0)
            return parseMeBean(uid, result.getJSONObject("data"))

        return null
    }
}
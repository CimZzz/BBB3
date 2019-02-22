package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseAliCashBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class ApplyCashRequest (
    val uid: String,
    val price: Double,
    val aliAccount: String,
    val name: String,
    val mobile: String
): BaseRequest<JSONObject>(Http.Apply.CASH) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("applyWithdraw") {
                it["uid"] = uid
                it["zhifubaoAccount"] = aliAccount
                it["realName"] = name
                it["num"] = price
                it["mobile"] = mobile
            }))

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response, true)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0)
            return parseAliCashBean(result)
        else configErrorTxt(result.getString("message"))

        return null
    }
}
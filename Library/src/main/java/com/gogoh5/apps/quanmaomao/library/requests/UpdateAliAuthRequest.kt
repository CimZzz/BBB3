package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseUserBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class UpdateAliAuthRequest(
    val uid: String,
    val nick: String?,
    val avatar: String?,
    val openId: String?,
    val openSid: String?,
    val topAccessToken: String?,
    val topAuthCode: String?,
    val topExpireTime: String?
): BaseRequest<JSONObject>(Http.Update.ALI_AUTH) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("uploadTaoAuth") {
                it["uid"] = uid
                it["nick"] = nick
                it["avatarUrl"] = avatar
                it["openId"] = openId
                it["openSid"] = openSid
                it["topAccessToken"] = topAccessToken
                it["topAuthCode"] = topAuthCode
                it["topExpireTime"] = topExpireTime
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


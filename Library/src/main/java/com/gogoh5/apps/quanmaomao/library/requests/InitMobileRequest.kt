package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseInitBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildUrl
import okhttp3.Request
import okhttp3.Response

class InitMobileRequest(
    val uid: String,
    val sex: Int,
    val createTime: Long
): BaseRequest<JSONObject>(Http.Init.MOBILE) {
    override fun buildRequest(): Request.Builder {
        return Request.Builder()
            .url(buildUrl(url) {
                it["appVersion"] = Constants.APP_VERSION_CODE
                it["model"] = Constants.DEVICE_MODE
                it["manufacture"] = Constants.DEVICE_MANUFACTURE
                it["version"] = Constants.DEVICE_VERSION
                it["uid"] = uid
                it["sex"] = sex
                it["createTime"] = createTime
            })
    }

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response)




    override fun analyzeResult(result: JSONObject?): Any? {
        return parseInitBean(result)
    }
}
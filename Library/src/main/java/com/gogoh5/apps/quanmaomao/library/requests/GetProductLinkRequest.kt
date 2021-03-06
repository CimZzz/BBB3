package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseProductDetailBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildUrl
import okhttp3.Request
import okhttp3.Response

class GetProductLinkRequest(
    val uid: String,
    val itemId: String
): BaseRequest<JSONObject>(Http.Get.PRODUCT_LINK) {
    override fun buildRequest(): Request.Builder {
        return Request.Builder()
            .url(buildUrl(url) {
                it["uid"] = uid
                it["itemId"] = itemId
            })
    }

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0) {
            val dataObj = result.getJSONObject("data")?:return null
            return dataObj.getString("link")
        }

        return parseProductDetailBean(result)
    }

}
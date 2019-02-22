package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseBalanceRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildApiEncode
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.*

class GetBalanceListRequest(
    val uid: String,
    val page: Int,
    val pageSize: Int = 20
): BaseRequest<JSONObject>(Http.Get.DETAIL_BALANCE) {
    override fun buildRequest(): Request.Builder =
        Request.Builder()
            .url(url)
            .post(RequestBody.create(null, buildApiEncode("balanceList") {
                it["uid"] = uid
                it["page"] = page
                it["pageSize"] = pageSize
            }))

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response, true)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        if(result.getIntValue("status") == 0) {
            val list = LinkedList<BaseRenderer>()
            val dataArr = result.getJSONArray("dataArr")
            if(dataArr != null && dataArr.size != 0) {
                dataArr.filterIsInstance(JSONObject::class.java).forEach {
                    list.add(parseBalanceRenderer(it)?:return@forEach)
                }
            }

            return ListBean(list, list.isEmpty() || result.getBooleanValue("isOver"), page)
        }

        return null
    }
}
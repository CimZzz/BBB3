package com.gogoh5.apps.quanmaomao.library.requests

import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.parseProductBean
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.buildUrl
import okhttp3.Request
import okhttp3.Response

class GetProductListRequest(
    val uid: String,
    val page: Int,
    val order: Int? = null,
    val k: String? = null,
    val startprice: Double? = null,
    val endprice: Double? = null,
    val isTmall: Boolean = true
): BaseRequest<JSONObject>(Http.Get.PRODUCT_LIST) {
    override fun buildRequest(): Request.Builder {
        return Request.Builder()
            .url(buildUrl(url) {
                it["uid"] = uid
                it["page"] = page
                it["order"] = order
                it["k"] = k
                it["startprice"] = startprice
                it["endprice"] = endprice
                it["tmall"] = isTmall
            })
    }

    override fun analyzeResponse(response: Response): JSONObject? = getJSONObjectFromResponse(response)

    override fun analyzeResult(result: JSONObject?): Any? {
        if(result == null)
            return null

        val dataArr = result.getJSONArray("d")
        var isOver = result.getBooleanValue("isOver")

        val productList = ArrayList<ProductBean>()

        if(dataArr != null) {
            dataArr.filterIsInstance(JSONObject::class.java).forEach {
                val bean = parseProductBean(it)
                if(bean != null)
                    productList.add(bean)
            }
        }

        if(productList.isEmpty())
            isOver = true

        return ListBean(productList, isOver, page)
    }

}
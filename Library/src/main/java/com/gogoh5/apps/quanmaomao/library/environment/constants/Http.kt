package com.gogoh5.apps.quanmaomao.library.environment.constants

import com.gogoh5.apps.quanmaomao.library.toolkits.UrlPath
import com.gogoh5.apps.quanmaomao.library.toolkits.pathOf
import com.gogoh5.apps.quanmaomao.library.toolkits.urlOf
import okhttp3.MediaType

object Http {
    object ContentType {
        val JSON = MediaType.get("application/json")
    }

    object Init {
        val MOBILE = pathOf("/mobileInit")
        val ME = pathOf("/mePage/api")
    }

    object Get {
        val PRODUCT_LIST = pathOf("/search/base")
        val PRODUCT_DETAIL = pathOf("/product/detail")
        val PRODUCT_LINK = pathOf("/coupon")

        val ALI_SC_AUTH_STATUS = pathOf("/user/api ")

        val WX_ACCESS_TOKEN = urlOf("https://api.weixin.qq.com/sns/oauth2/access_token")
        val WX_USER_INFO = urlOf("https://api.weixin.qq.com/sns/userinfo")

        val DETAIL_BALANCE = pathOf("/mePage/api")
    }

    object Update {
        val ALI_AUTH = pathOf("/user/api")

        val WX_AUTH = pathOf("/user/api")
    }

    object Apply {
        val CASH = pathOf("/mePage/api")
    }



    fun checkHost() {
        Http.Init.MOBILE with Constants.HOST
        Http.Init.ME with Constants.HOST

        Http.Get.PRODUCT_LIST with Constants.HOST
        Http.Get.PRODUCT_DETAIL with Constants.HOST
        Http.Get.PRODUCT_LINK with Constants.HOST

        Http.Get.ALI_SC_AUTH_STATUS with Constants.HOST

        Http.Get.DETAIL_BALANCE with Constants.HOST

        Http.Update.ALI_AUTH with Constants.HOST
        Http.Update.WX_AUTH with Constants.HOST

        Http.Apply.CASH with Constants.HOST
    }
}
package com.gogoh5.apps.quanmaomao.library.environment.constants

import android.os.Build
import com.gogoh5.apps.quanmaomao.library.BuildConfig

object Constants {
    const val APP_VERSION_CODE = BuildConfig.VERSION_CODE
    const val APP_VERSION_NAME = BuildConfig.VERSION_NAME
    val DEVICE_MODE = Build.MODEL
    val DEVICE_MANUFACTURE = Build.MANUFACTURER
    val DEVICE_VERSION = Build.VERSION.SDK_INT

    val EXTERNAL_ROOT = "quanmaomao/com.gogoh5.apps.quanmaomao"
    val DIR_GLIDE = "glide"

    const val WX_APP_ID = "wxbe94737ead5c3161"
    const val WX_APP_SECRET = "b9d2dcda854235222b69777ae3d96e78"

    lateinit var HOST: String
    const val TEST_HOST = "http://testqmm.gogoh5.com:9000"


    fun checkHost(host: String) {
        HOST = host
        Http.checkHost()
    }
}
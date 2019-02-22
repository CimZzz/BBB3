package com.gogoh5.apps.quanmaomao.library.environment

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeAsyncCallback
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz

class CustomApplication: MultiDexApplication() {
    companion object {
        lateinit var instance: CustomApplication
            internal set
    }

    override fun onCreate() {
        super.onCreate()

        Constants.checkHost(Constants.TEST_HOST)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this

        AlibcTradeSDK.asyncInit(this, object: AlibcTaokeAsyncCallback, AlibcTradeInitCallback {
            override fun onSuccess() {
                logCimZzz("百川初始化成功")
            }

            override fun onFailure(p0: Int, p1: String?) {
                logCimZzz("百川初始化失败 $p0 $p1")
            }

        })
    }
}
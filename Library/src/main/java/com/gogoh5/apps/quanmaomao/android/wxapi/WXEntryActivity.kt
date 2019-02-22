package com.gogoh5.apps.quanmaomao.android.wxapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/01/12 01:24:35
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class WXEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SysContext.getWeChat().handleIntent(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        SysContext.getWeChat().handleIntent(intent)
        finish()
    }

}
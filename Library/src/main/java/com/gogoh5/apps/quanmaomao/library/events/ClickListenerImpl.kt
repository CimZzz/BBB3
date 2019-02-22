package com.gogoh5.apps.quanmaomao.library.events

import android.view.View
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRun

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/14 01:18:21
 *  Project : taoke_android
 *  Since Version : Alpha
 */
open class ClickListenerImpl(val name: String? = null, val actionSource: ActionSource, val takeRun: TakeRun<View?>) : View.OnClickListener {
    override fun onClick(v: View?) {
        takeRun(v)
    }
}
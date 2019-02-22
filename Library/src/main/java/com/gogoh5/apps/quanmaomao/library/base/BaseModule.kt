package com.gogoh5.apps.quanmaomao.library.base

import android.content.Context
import android.content.SharedPreferences
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

abstract class BaseModule {

    protected fun getSharePreferences(spName: String): SharedPreferences =
            SysContext.getApp().getSharedPreferences(spName, Context.MODE_PRIVATE)
}
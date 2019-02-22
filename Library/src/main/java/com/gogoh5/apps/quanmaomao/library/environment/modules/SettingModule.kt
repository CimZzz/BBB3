package com.gogoh5.apps.quanmaomao.library.environment.modules

import android.content.Context
import android.content.SharedPreferences
import com.gogoh5.apps.quanmaomao.library.entities.databeans.InitBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.SharedPreferenceType
import com.gogoh5.apps.quanmaomao.library.toolkits.HunterManager

class SettingModule private constructor() {
    companion object {
        const val INIT = 0

        internal val instance: SettingModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SettingModule()
        }
    }

    val hunterManager = HunterManager()
    private val sharedPreference: SharedPreferences =
        SysContext.getApp().getSharedPreferences(SharedPreferenceType.TYPE_SETTING, Context.MODE_PRIVATE)


    var initBean = InitBean.EMPTY

    fun resetInitBean() {
        initBean = InitBean.EMPTY
    }

    fun configInitBean(initBean: InitBean) {
        this.initBean = initBean
        hunterManager.doEat(INIT)
    }
}

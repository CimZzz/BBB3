package com.gogoh5.apps.quanmaomao.library.environment.modules

import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.toolkits.HunterManager

class BusModule private constructor(): BaseModule() {
    companion object {
        /**
         * isSuccess(Boolean) message(String?)
         */
        const val TYPE_ALI_SC_AUTH = 0


        /**
         * isSuccess(Boolean) Message(String|when false)
         */
        const val TYPE_ALI_AUTH = 1

        internal val instance: BusModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { BusModule() }
    }

    val hunterManager: HunterManager = HunterManager()


    fun aliScAuth(isSuccess: Boolean, message: String?) {
        hunterManager.doEat(TYPE_ALI_SC_AUTH, isSuccess, message?:"")
    }

    /*Ali Auth*/
    fun handleAliAuth(isSuccess: Boolean, msg: String? = null) {
        if(isSuccess) {
            val session = AlibcLogin.getInstance()?.session
            if(session == null) {
                hunterManager.doEat(TYPE_ALI_AUTH, false, "淘宝授权失败")
                return
            }

            hunterManager.doEat(TYPE_ALI_AUTH, true)
            return
        }

        hunterManager.doEat(TYPE_ALI_AUTH, false, msg?:"淘宝授权失败")
    }
}
package com.gogoh5.apps.quanmaomao.library.environment.modules

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.UserBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.SharedPreferenceType
import com.gogoh5.apps.quanmaomao.library.extended.spdelegate.*
import com.gogoh5.apps.quanmaomao.library.requests.InitMeRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.*
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz
import java.util.*

class UserModule private constructor(): BaseModule() {
    companion object {
        /**
         * isSuccess(Boolean) MeBean
         */
        const val TYPE_ME_BEAN = 0

        /**
         * None
         */
        const val TYPE_ALL = 1

        /**
         * NickName(String)
         */
        const val TYPE_NICK_NAME = 2

        /**
         * ShortId(String)
         */
        const val TYPE_SHORT_ID = 3

        /**
         * Avatar(String)
         */
        const val TYPE_AVATAR = 4

        internal val instance: UserModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserModule()
        }
    }

    val hunterManager: HunterManager = HunterManager()
    val method = UserMethod(this)
    private val sharedPreference = getSharePreferences(SharedPreferenceType.TYPE_USER)
    private var isAllowChangeBack = true

    var uid by StringSPNotNullDelegate(sharedPreference, "uid", {UUID.randomUUID().toString()})
    var sex by IntSPDelegate(sharedPreference, "sex", {2})
    var createTime by LongSPDelegate(sharedPreference, "createTime", {System.currentTimeMillis()})

    var nickName by StringSPDelegate(sharedPreference, "nickName")  {
        if(isAllowChangeBack)
            hunterManager.doEat(TYPE_NICK_NAME, it?:"")
    }

    var shortId by StringSPDelegate(sharedPreference, "shortId") {
        if(isAllowChangeBack)
            hunterManager.doEat(TYPE_SHORT_ID, it?:"")
    }

    var avatar by StringSPDelegate(sharedPreference, "avatar") {
        if(isAllowChangeBack)
            hunterManager.doEat(TYPE_AVATAR, it?:"")
    }

    var isAliAuth by BooleanSPDelegate(sharedPreference, "isAliAuth", {false})

    fun updateFromAliAuth(userBean: UserBean) {
        if(userBean.uid != uid) {
            isAllowChangeBack = false
            SysContext.getHttp().reInit()
            SysContext.getSetting().resetInitBean()
            meBean = null
            method.resetMeBeanRequest()
        }

        uid = userBean.uid
        nickName = userBean.name
        sex = userBean.sex
        avatar = userBean.avatar
        shortId = userBean.sid
        isAliAuth = true

        if(!isAllowChangeBack)
            hunterManager.doEat(TYPE_ALL)
    }

    var isWxAuth by BooleanSPDelegate(sharedPreference, "isWxAuth", {false})

    fun updateFromWxAuth(userBean: UserBean) {
        nickName = userBean.name
        sex = userBean.sex
        avatar = userBean.avatar
        shortId = userBean.sid

        isWxAuth = true
    }

    /*Me Bean*/
    private var meBean: MeBean? = null

    fun getMeBean(isNullRefresh: Boolean =  true): MeBean? {
        if(meBean == null && isNullRefresh) {
            method.requestMeBean()
            return null
        }

        return meBean
    }

    fun refreshMeBean() {
        method.requestMeBean()
    }

    internal fun onMeBeanCallback(meBean: MeBean?) {
        if(meBean != null) {
            this.meBean = meBean
            hunterManager.doEat(TYPE_ME_BEAN, true, meBean)
        }
        else hunterManager.doEat(TYPE_ME_BEAN, false, Unit)
    }

    fun doCashAction(price: Double): Double {
        val meBean = meBean?:return 0.0
        meBean.balance -= price
        meBean.totalWithDraw += price
        hunterManager.doEat(TYPE_ME_BEAN, true, meBean)

        return meBean.balance
    }

}

class UserMethod(userModule: UserModule) : BaseMethod<UserModule>(userModule) {
    val meBeanSyncCode = SyncCode()
    fun resetMeBeanRequest() {
        meBeanSyncCode.nextCode()
    }

    fun requestMeBean() {
        appendRequest(0, InitMeRequest(SysContext.getUser().uid)
            .dataHunter(RefDataHunter(presenterRef, syncRuleGroup = SyncRuleGroup(arrayOf(SyncRule(meBeanSyncCode.code, meBeanSyncCode)))) {
                userModule, params ->
                if(params[0] as Boolean)
                    userModule.onMeBeanCallback(params[1] as MeBean)
                else userModule.onMeBeanCallback(null)
            })
        )
    }
}
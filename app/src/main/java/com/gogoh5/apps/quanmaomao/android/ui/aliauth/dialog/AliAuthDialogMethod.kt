package com.gogoh5.apps.quanmaomao.android.ui.aliauth.dialog

import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.entities.databeans.UserBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.BusModule
import com.gogoh5.apps.quanmaomao.library.environment.modules.UserModule
import com.gogoh5.apps.quanmaomao.library.requests.UpdateAliAuthRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter

class AliAuthDialogMethod(aliAuthDialogPresenter: AliAuthDialogPresenter) : BaseMethod<AliAuthDialogPresenter>(aliAuthDialogPresenter) {

    init {
        SysContext.getBus().hunterManager.register("AliAuthDialogMethod", RefDataHunter(presenterRef) {
            presenter, params->
            when(params[0] as Int) {
                BusModule.TYPE_ALI_AUTH -> {
                    if(params[1] as Boolean)
                        presenter.doAuth()
                    else presenter.onAuthFailure(params[2] as String)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SysContext.getBus().hunterManager.unregister("AliAuthDialogMethod")
    }


    fun checkAliAuth(): Boolean = SysContext.checkAliAuth()

    fun updateUserInfo() {
        val session = AlibcLogin.getInstance().session
        appendRequest(0,
            UpdateAliAuthRequest(
                SysContext.getUser().uid,
                session.nick,
                session.avatarUrl,
                session.openId,
                session.openSid,
                session.topAccessToken,
                session.topAuthCode,
                session.topExpireTime)
                .dataHunter(RefDataHunter(presenterRef) {
                    presenter, params->
                    if(params[0] as Boolean) {
                        SysContext.getUser().updateFromAliAuth(params[1] as UserBean)
                        presenter.onUpdateSuccess()
                    }
                    else presenter.onUpdateFailure(params[1] as String)
                })
        )
    }

}
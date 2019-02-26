package com.gogoh5.apps.quanmaomao.android.ui.setting

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.entities.databeans.UserBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.WxAccessTokenBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.WxUserInfoBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.WeChatModule
import com.gogoh5.apps.quanmaomao.library.requests.GetWxAccessTokenRequest
import com.gogoh5.apps.quanmaomao.library.requests.GetWxUserInfoRequest
import com.gogoh5.apps.quanmaomao.library.requests.UpdateWxAuthRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.FileUtils
import java.lang.Exception

class SettingMethod(settingPresenter: SettingPresenter) : BaseMethod<SettingPresenter>(settingPresenter) {
    init {
        SysContext.getWeChat().hunterManager.register("SettingMethod", RefDataHunter(presenterRef) {
            presenter, params ->
            when(params[0] as Int) {
                WeChatModule.TYPE_AUTH -> {
                    if(params[1] as Boolean) {
                        presenter.showWxAuthDialog()
                        onWxCodeCallback(params[2] as String)
                    }
                    else presenter.onWxAuthFailure()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SysContext.getWeChat().hunterManager.unregister("SettingMethod")
    }

    fun calculateCache() {
        SysContext.getThread().sendRunnableWithHunter(RefDataHunter(presenterRef) {
            presenter, params->
            presenter.onCalculateCallback(params[0] as Long)
        }) {
            try {
                FileUtils.calcByteCount(SysContext.getFile().getRootCacheDir())
            }
            catch (e: Exception) {
                0L
            }

        }
    }

    fun checkWxAuth(): Boolean = SysContext.getUser().isWxAuth

    fun authWx() {
        SysContext.getWeChat().sendAuth()
    }

    fun unAuthWx() {
        SysContext.getUser().isWxAuth = false
    }

    private fun onWxCodeCallback(code: String) {
        appendRequest(0, GetWxAccessTokenRequest(code)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params ->
                if(params[0] as Boolean)
                    onWxAccessTokenCallback(params[1] as WxAccessTokenBean)
                else presenter.onWxAuthFailure()
            })
        )
    }

    private fun onWxAccessTokenCallback(wxAccessTokenBean: WxAccessTokenBean) {
        appendRequest(1, GetWxUserInfoRequest(wxAccessTokenBean.accessToken, wxAccessTokenBean.openid)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params ->
                if(params[0] as Boolean)
                    onWxUserInfoCallback(params[1] as WxUserInfoBean)
                else presenter.onWxAuthFailure()
            })
        )
    }

    private fun onWxUserInfoCallback(wxUserInfoBean: WxUserInfoBean) {
        appendRequest(2, UpdateWxAuthRequest(
            SysContext.getUser().uid,
            wxUserInfoBean.unionId,
            wxUserInfoBean.openId,
            wxUserInfoBean.nickname,
            wxUserInfoBean.sex,
            wxUserInfoBean.province,
            wxUserInfoBean.city,
            wxUserInfoBean.country,
            wxUserInfoBean.headimgurl,
            wxUserInfoBean.privilege
        )
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params ->
                if(params[0] as Boolean) {
                    SysContext.getUser().updateFromWxAuth(params[1] as UserBean)
                    presenter.onWxAuthSuccess()
                }
                else presenter.onWxAuthFailure(params[1] as String)
            })
        )
    }

    fun clearCache() {
        SysContext.getThread().sendRunnableWithHunter(RefDataHunter(presenterRef) {
            presenter, _->
            presenter.onClearCacheCallback()
        }) {
            try {
                SysContext.getGlide().clear()
                FileUtils.delete(SysContext.getFile().getRootCacheDir())
            }
            catch (e: Exception) {
            }
        }
    }
}
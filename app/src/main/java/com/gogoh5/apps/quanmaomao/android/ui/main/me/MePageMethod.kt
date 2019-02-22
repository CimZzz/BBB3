package com.gogoh5.apps.quanmaomao.android.ui.main.me

import com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen.MeLazyLoadBean
import com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen.StringLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.UserModule
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz

class MePageMethod(mePagePresenter: MePagePresenter) : BaseMethod<MePagePresenter>(mePagePresenter) {
    init {
        SysContext.getUser().hunterManager.register("MePageMethod", RefDataHunter(presenterRef) {
            presenter, params->
            when(params[0] as Int) {
                UserModule.TYPE_NICK_NAME -> {
                    presenter.putLazyLoadParams(
                        MePage.LAZY_LOAD_NICK_NAME, StringLazyLoadBean(StringUtils.convertToNull(params[1] as String))
                    )
                }
                UserModule.TYPE_SHORT_ID -> {
                    presenter.putLazyLoadParams(
                        MePage.LAZY_LOAD_SHORT_ID, StringLazyLoadBean(StringUtils.convertToNull(params[1] as String))
                    )
                }
                UserModule.TYPE_AVATAR -> {
                    presenter.putLazyLoadParams(
                        MePage.LAZY_LOAD_AVATAR, StringLazyLoadBean(StringUtils.convertToNull(params[1] as String))
                    )
                }
                UserModule.TYPE_ALL -> {
                    presenter.refreshMeBean()
                }
                UserModule.TYPE_ME_BEAN -> {
                    val isSuccess = params[1] as Boolean
                    if(isSuccess)
                        presenter.putLazyLoadParams(
                            MePage.LAZY_LOAD_MONEY_INFO,
                            MeLazyLoadBean(BaseLazyLoadBean.STATUS_SUCCESS, params[2] as MeBean)
                        )
                    else {
                        presenter.putLazyLoadParams(
                            MePage.LAZY_LOAD_MONEY_INFO,
                            MeLazyLoadBean(BaseLazyLoadBean.STATUS_FAILURE, null)
                        )
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SysContext.getUser().hunterManager.unregister("MePageMethod")
    }

    fun initData() {
        val userModule = SysContext.getUser()
        val presenter = presenterRef.get()?:return

        val meBean = userModule.getMeBean(true)
        if(meBean != null) {
            presenter.putLazyLoadParams(
                MePage.LAZY_LOAD_MONEY_INFO,
                MeLazyLoadBean(BaseLazyLoadBean.STATUS_SUCCESS, meBean)
            )
        }

        val nickName = userModule.nickName
        val shortId = userModule.shortId
        val avatar = userModule.avatar

        presenter.putLazyLoadParams(
            MePage.LAZY_LOAD_NICK_NAME, StringLazyLoadBean(nickName)
        )
        presenter.putLazyLoadParams(
            MePage.LAZY_LOAD_SHORT_ID, StringLazyLoadBean(shortId)
        )
        presenter.putLazyLoadParams(
            MePage.LAZY_LOAD_AVATAR, StringLazyLoadBean(avatar)
        )
    }

    fun refreshMeBean() {
        SysContext.getUser().refreshMeBean()
    }
}
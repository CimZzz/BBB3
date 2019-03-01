package com.gogoh5.apps.quanmaomao.android.ui.main

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.SettingModule
import com.gogoh5.apps.quanmaomao.library.environment.modules.UserModule
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter

class MainMethod(mainPresenter: MainPresenter) : BaseMethod<MainPresenter>(mainPresenter) {
    init {
        SysContext.getUser().hunterManager.register("MainMethod", RefDataHunter(presenterRef) {
            presenter, params->
            when(params[0] as Int) {
                UserModule.TYPE_ALL -> presenter.refreshAll()
            }
        })

        SysContext.getSetting().hunterManager.register("MainMethod", RefDataHunter(presenterRef) {
            presenter, params->
            when(params[0] as Int) {
                SettingModule.INIT -> presenter.refreshAll()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SysContext.getUser().hunterManager.unregister("MainMethod")
        SysContext.getSetting().hunterManager.unregister("MainMethod")
    }

    fun checkAliAuth() = SysContext.getUser().isAliAuth
}
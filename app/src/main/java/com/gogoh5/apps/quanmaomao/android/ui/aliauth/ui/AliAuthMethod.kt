package com.gogoh5.apps.quanmaomao.android.ui.aliauth.ui

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class AliAuthMethod(presenter: AliAuthPresenter) : BaseMethod<AliAuthPresenter>(presenter) {
    fun handleResult(isSuccess: Boolean, msg: String? = null) {
        SysContext.getBus().handleAliAuth(isSuccess, msg)
    }
}
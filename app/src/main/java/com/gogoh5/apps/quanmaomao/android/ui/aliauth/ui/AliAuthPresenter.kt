package com.gogoh5.apps.quanmaomao.android.ui.aliauth.ui

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle

class AliAuthPresenter(view: IAliAuthView) : BaseMethodPresenter<IAliAuthView, AliAuthMethod>(view) {
    override fun generateMethod(): AliAuthMethod = AliAuthMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
    }

    override fun onViewEnter() {
        super.onViewEnter()
        if(!markDone) {
            markDoneAction(true)
            view.callAliAuth()
        }
    }

    fun onAliAuthCallback(isSuccess: Boolean, msg: String? = null) {
        method.handleResult(isSuccess, msg)
        view.closeDirectly()
    }
}
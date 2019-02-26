package com.gogoh5.apps.quanmaomao.android.ui.aliauth.dialog

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class AliAuthDialogPresenter(view: IAliAuthDialogView) : BaseMethodPresenter<IAliAuthDialogView, AliAuthDialogMethod>(view) {
    override fun generateMethod(): AliAuthDialogMethod =
        AliAuthDialogMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {

    }

    fun doAuth() {
        if(!method.checkAliAuth()) {
            LinkUtils.linkAliAuth()
            return
        }

        view.showWaitDialog("绑定用户中...")
        method.updateUserInfo()
    }


    fun onAuthFailure(msg: String) {
        sendToast(msg)
    }

    fun onUpdateSuccess() {
        sendToast("绑定成功")
        view.closeWaitDialog()
        view.closeDirectly()
    }

    fun onUpdateFailure(msg: String?) {
        sendToast(msg)
        view.closeWaitDialog()
    }

    fun linkProtocol() {
        LinkUtils.linkWeb(Http.H5.PROTOCOL.url, context = view.getContext())
    }

    fun linkMain() {
        LinkUtils.linkMain(isClose = true, context = view.getContext())
    }
}
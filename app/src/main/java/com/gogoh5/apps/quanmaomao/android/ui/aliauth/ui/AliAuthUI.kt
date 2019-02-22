package com.gogoh5.apps.quanmaomao.android.ui.aliauth.ui

import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.gogoh5.apps.quanmaomao.library.base.BaseUI

class AliAuthUI: BaseUI<AliAuthPresenter>(), IAliAuthView, AlibcLoginCallback {

    override fun initPresenter(): AliAuthPresenter = AliAuthPresenter(this)

    override fun initView() {
    }

    override fun callAliAuth() {
        showWaitDialog("淘宝授权中...")
        AlibcLogin.getInstance().showLogin(this)
    }

    override fun onSuccess(code: Int) {
        closeWaitDialog()
        presenter.onAliAuthCallback(true)
    }

    override fun onFailure(code: Int, msg: String?) {
        closeWaitDialog()
        presenter.onAliAuthCallback(false, msg)
    }

    override fun onBackPressed() {

    }
}
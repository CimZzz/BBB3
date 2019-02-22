package com.gogoh5.apps.quanmaomao.android.ui.aliauth.dialog

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import kotlinx.android.synthetic.main.ui_ali_auth.*

class AliAuthDialogUI: BaseUI<AliAuthDialogPresenter>(),
    IAliAuthDialogView {
    override fun initPresenter(): AliAuthDialogPresenter =
        AliAuthDialogPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_ali_auth)

        cancelBtn.tapWith("淘宝授权页返回按钮") {
            LinkUtils.linkMain(isClose = true, context = this)
            closeDirectly()
        }

        agreeBtn.tapWith("淘宝授权页同意按钮") {
            presenter.doAuth()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
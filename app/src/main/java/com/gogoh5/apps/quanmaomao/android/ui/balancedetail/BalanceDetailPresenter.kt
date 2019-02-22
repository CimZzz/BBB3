package com.gogoh5.apps.quanmaomao.android.ui.balancedetail

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle

class BalanceDetailPresenter(view: IBalanceDetailView) :
    BaseMethodPresenter<IBalanceDetailView, BalanceDetailMethod>(view) {
    override fun generateMethod(): BalanceDetailMethod = BalanceDetailMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
    }
}
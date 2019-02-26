package com.gogoh5.apps.quanmaomao.android.ui.cashlist

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle

class CashListPresenter(view: ICashListView) : BaseMethodPresenter<ICashListView, CashListMethod>(view) {

    override fun generateMethod(): CashListMethod = CashListMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {

    }

}
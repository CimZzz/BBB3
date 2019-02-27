package com.gogoh5.apps.quanmaomao.test.uis.bootstraip

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle

class BootStrapPresenter(view: IBootStrapView) : BaseMethodPresenter<IBootStrapView, BootStrapMethod>(view) {

    override fun generateMethod(): BootStrapMethod = BootStrapMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {

    }

    override fun onViewEnter() {
        view.checkEnable()
    }
}
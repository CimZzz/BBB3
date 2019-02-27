package com.gogoh5.apps.quanmaomao.android.ui.launch

import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class LaunchPresenter(view: ILaunchView) : BaseMethodPresenter<ILaunchView, LaunchMethod>(view) {
    override fun generateMethod(): LaunchMethod = LaunchMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        method.checkHttpInit()
        method.startCountDown()
    }

    override fun onLazyLoad() {
        getLazyLoadParams(LaunchUI.LAZY_LOAD_TIME_OUT)?:return
        getLazyLoadParams(LaunchUI.LAZY_LOAD_PERMISSION)?:return

        LinkUtils.linkMain(context = view.getContext())
        view.closeDirectly()
    }
}
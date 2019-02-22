package com.gogoh5.apps.quanmaomao.android.ui.launch

import com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen.EmptyLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.toolkits.CountDownTimer
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter

class LaunchMethod(presenter: LaunchPresenter) : BaseMethod<LaunchPresenter>(presenter) {

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    fun checkHttpInit() {
        SysContext.getHttp().checkInit()
    }


    private var timer: CountDownTimer? = null
    fun startCountDown() {
        stopCountDown()
        timer = CountDownTimer(LaunchUI.TIME_OUT, LaunchUI.TIME_OUT, RefDataHunter(presenterRef) {
            presenter, params->

            if(params[0] as Boolean)
                presenter.putLazyLoadParams(LaunchUI.LAZY_LOAD_TIME_OUT, BaseLazyLoadBean.EMPTY)
        })
        timer?.start()
    }

    private fun stopCountDown() {
        timer?.cancel()
        timer = null
    }
}
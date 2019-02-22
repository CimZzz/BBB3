package com.gogoh5.apps.quanmaomao.android.ui.cash

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.entities.databeans.AliCashBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.requests.ApplyCashRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter

class CashMethod(presenter: CashPresenter) : BaseMethod<CashPresenter>(presenter) {
    fun getBalance(): Double = SysContext.getUser().getMeBean()?.balance?:0.0

    fun doCash(price: Double, aliAccountStr: String, nameStr: String, mobileStr: String) {
        appendRequest(0, ApplyCashRequest(SysContext.getUser().uid, price, aliAccountStr, nameStr, mobileStr)
            .dataHunter(RefDataHunter(presenterRef) {
                presenter, params ->
                if(params[0] as Boolean) {
                    val aliCashBean = params[1] as AliCashBean
                    val user = SysContext.getUser()

                    presenter.onCashSuccess(aliCashBean.status, user.doCashAction(aliCashBean.price))
                }
                else presenter.onCashFailure(params[1] as String)
            })
        )
    }
}
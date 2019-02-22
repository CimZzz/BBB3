package com.gogoh5.apps.quanmaomao.android.ui.cash

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle

class CashPresenter(view: ICashView) : BaseMethodPresenter<ICashView, CashMethod>(view) {
    override fun generateMethod(): CashMethod = CashMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        view.configBalance(method.getBalance())
    }

    fun linkBalanceDetail() {

    }

    fun configAllBalance() {
        view.configPrice(method.getBalance())
    }

    fun doCash() {
        val price = view.getPriceStr().toDoubleOrNull()
        if(price == null) {
            sendToast("提现金额不是一个数字")
            return
        }

        if(price < 1) {
            sendToast("提现金额必须大于1元")
            return
        }

        val aliAccountStr = view.getAliAccountStr()
        if(aliAccountStr.isBlank()) {
            sendToast("请输入支付宝账号")
            return
        }

        val nameStr = view.getNameStr()
        if(nameStr.isBlank()) {
            sendToast("请输入与支付宝账号相匹配的姓名")
            return
        }

        val mobileStr = view.getMobileStr()
        if(mobileStr.isBlank()) {
            sendToast("请输入您的手机号")
            return
        }

        view.showWaitDialog("提现中...请稍后")
        method.doCash(price, aliAccountStr, nameStr, mobileStr)
    }

    fun onCashSuccess(status: String?, balance: Double) {
        if(status.isNullOrEmpty())
            sendToast("提现成功")
        else sendToast("提现成功 $status")
        view.closeWaitDialog()

        view.configPrice(0.0)
        view.configBalance(balance)
    }

    fun onCashFailure(message: String) {
        view.closeWaitDialog()
        sendToast(message)
    }
}
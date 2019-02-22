package com.gogoh5.apps.quanmaomao.android.ui.cash

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.selectionEnd
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import kotlinx.android.synthetic.main.ui_cash.*

class CashUI: BaseUI<CashPresenter>(), ICashView {
    override fun initPresenter(): CashPresenter = CashPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_cash)

        backBtn.tapWith("提现页返回按钮", ActionSource.CASH_BACK) {
            closeDirectly()
        }

        balanceDetailBtn.tapWith("提现页余额明细", ActionSource.CASH_BALANCE) {
            presenter.linkBalanceDetail()
        }

        allCashBtn.tapWith("提现页全部提现", ActionSource.CASH_BALANCE_ALL) {
            presenter.configAllBalance()
        }

        confirmBtn.tapWith("提现页提现按钮", ActionSource.CASH_CASH) {
            presenter.doCash()
        }
    }

    override fun configBalance(balance: Double) {
        balanceTxt.text = StringUtils.formatPrice(balance)
    }

    override fun configPrice(price: Double) {
        priceTxt.setText(StringUtils.formatPrice(price))
        priceTxt.selectionEnd()
    }

    override fun getPriceStr(): String = priceTxt.text.toString()

    override fun getAliAccountStr(): String = aliAccountTxt.text.toString()

    override fun getNameStr(): String = nameTxt.text.toString()

    override fun getMobileStr(): String = mobileTxt.text.toString()
}
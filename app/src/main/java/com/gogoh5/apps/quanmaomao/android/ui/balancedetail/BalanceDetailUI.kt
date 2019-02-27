package com.gogoh5.apps.quanmaomao.android.ui.balancedetail

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPage
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageDataBundle
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.ui_balance_detail.*

class BalanceDetailUI: BaseUI<BalanceDetailPresenter>(), IBalanceDetailView {

    private lateinit var listPage: ListPage
    private lateinit var pageContext: BalanceListContext


    override fun initPresenter(): BalanceDetailPresenter = BalanceDetailPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_balance_detail)

        backBtn.tapWith("余额明细返回按钮", ActionSource.BALANCE_DETAIL_BACK) {
            closeDirectly()
        }


        pageContext = BalanceListContext(this)
        listPage = ListPage(pageContext)
        listPage.initViewPage(pageContainer, listPage.generateDataParcelable(), 0)
        listPage.attachView(pageContainer)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus)
            listPage.enterPage()
        else listPage.quitPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        listPage.destroyPage()
    }
}
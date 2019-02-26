package com.gogoh5.apps.quanmaomao.android.ui.cashlist

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPage
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageDataBundle
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.ui_cash_list.*

class CashListUI: BaseUI<CashListPresenter>(), ICashListView {
    private lateinit var listPage: ListPage<ListPageDataBundle>
    private lateinit var pageContext: CashListPageContext

    override fun initPresenter(): CashListPresenter = CashListPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_cash_list)

        backBtn.tapWith("提现列表页返回按钮", ActionSource.CASH_LIST_BACK) {
            closeDirectly()
        }


        pageContext = CashListPageContext(this)
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
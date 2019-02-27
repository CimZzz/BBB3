package com.gogoh5.apps.quanmaomao.android.ui.search

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPage
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.ui_search.*

class SearchUI: BaseUI<SearchPresenter>(), ISearchView, SearchListContext.Callback {

    private lateinit var listPage: ListPage
    private lateinit var pageContext: SearchListContext

    override fun initPresenter(): SearchPresenter = SearchPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_search)

        backBtn.tapWith("搜索结果页返回按钮", ActionSource.SEARCH_BACK) {
            finish()
        }
        searchContentBg.tapWith("搜索结果页文本框", ActionSource.SEARCH_BAR) {
            finish()
        }

        pageContext = SearchListContext(this, this)
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

    override fun configPage(searchContent: String?) {
        searchContentTxt.text = searchContent
        pageContext.configSearchContent(searchContent)
    }

    override fun configSearchContent(txt: String) {
        searchContentTxt.text = txt
    }
}
package com.gogoh5.apps.quanmaomao.android.ui.main.search

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class SearchPageMethod(presenter: SearchPagePresenter) : BaseMethod<SearchPagePresenter>(presenter) {
    fun getHistory(): List<String>? = SysContext.getData().getSearchHistoryList()
    fun saveHistory(content: String) = SysContext.getData().pushSearchHistoryList(content)
    fun clearHistory() = SysContext.getData().clearSearchHistory()

    fun getHotSearch(): List<String>? = SysContext.getSetting().initBean.hotword

}
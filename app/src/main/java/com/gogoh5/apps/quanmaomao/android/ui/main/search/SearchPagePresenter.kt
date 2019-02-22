package com.gogoh5.apps.quanmaomao.android.ui.main.search

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class SearchPagePresenter(view: ISearchPageView) : BaseMethodPresenter<ISearchPageView, SearchPageMethod>(view) {

    override fun generateMethod(): SearchPageMethod = SearchPageMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        view.configHistory(method.getHistory())
        view.configHotSearch(method.getHotSearch())
    }

    fun clearHistory() {
        method.clearHistory()
        view.configHistory(method.getHistory())
    }

    fun openSearch(content: String) {
        if(!content.isBlank()) {
            method.saveHistory(content)
            view.configHistory(method.getHistory())
        }
        view.fillSearchBar(content)
        LinkUtils.linkSearchContent(content, view.getContext())
    }
}
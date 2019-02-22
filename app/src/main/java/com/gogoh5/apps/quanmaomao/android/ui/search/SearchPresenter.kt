package com.gogoh5.apps.quanmaomao.android.ui.search

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.links.SearchLink

class SearchPresenter(view: ISearchView) : BaseMethodPresenter<ISearchView, SearchMethod>(view) {

    override fun generateMethod(): SearchMethod = SearchMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        val link = view.getPassLink()
        if(link == null || link !is SearchLink) {
            view.closeDirectly()
            return
        }

        view.configPage(link.searchContent)
    }
}
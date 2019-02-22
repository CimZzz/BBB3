package com.gogoh5.apps.quanmaomao.android.ui.main.search

import android.content.Context
import com.gogoh5.apps.quanmaomao.library.base.IView

interface ISearchPageView: IView {
    fun getContext(): Context
    fun configHistory(historyArr: List<String>?)
    fun configHotSearch(hotSearchArr: List<String>?)
    fun fillSearchBar(content: String)
}
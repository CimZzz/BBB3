package com.gogoh5.apps.quanmaomao.library.extended.listview

import com.gogoh5.apps.quanmaomao.library.base.ILazyLoadView

interface IListPageView : ILazyLoadView<Unit> {
    fun showEmpty()
    fun collapseHeader(collapseHeader: Boolean, animate: Boolean)
    fun completeRefresh()
    fun attachFilterWindow()
    fun detachFilterWindow()
}
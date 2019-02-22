package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer

interface IListPageHeaderCreator {
    fun getHeaderType(data: Any): Int
    fun getViewController(parent: ViewGroup, position: Int, dataType: Int): ListPageBaseHeaderController<out BaseRenderer>?
}
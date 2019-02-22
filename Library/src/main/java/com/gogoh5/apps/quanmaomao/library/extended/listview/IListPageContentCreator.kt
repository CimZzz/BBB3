package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer

interface IListPageContentCreator {
    fun getContentType(data: Any): Int
    fun getViewHolder(parent: ViewGroup, dataType: Int): ListPageBaseContentHolder<out BaseRenderer>?
}
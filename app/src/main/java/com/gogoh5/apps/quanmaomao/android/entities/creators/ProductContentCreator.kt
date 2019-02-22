package com.gogoh5.apps.quanmaomao.android.entities.creators

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.holders.ProductHolder
import com.gogoh5.apps.quanmaomao.android.entities.renderers.ProductRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseContentHolder

object ProductContentCreator: IListPageContentCreator {

    override fun getContentType(data: Any): Int = when(data) {
        is ProductRenderer -> RendererType.CONTENT_PRODUCT
        else-> -1
    }

    override fun getViewHolder(parent: ViewGroup, dataType: Int): ListPageBaseContentHolder<out BaseRenderer>? = when(dataType) {
        RendererType.CONTENT_PRODUCT-> ProductHolder(parent)
        else-> null
    }
}
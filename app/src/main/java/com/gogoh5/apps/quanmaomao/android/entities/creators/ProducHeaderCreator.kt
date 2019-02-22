package com.gogoh5.apps.quanmaomao.android.entities.creators

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.controllers.HotSearchController
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HotSearchRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController

object ProducHeaderCreator: IListPageHeaderCreator {
    override fun getHeaderType(data: Any): Int =
        when(data) {
            is HotSearchRenderer -> RendererType.HEADER_HOT_SEARCH
            else -> -1
        }

    override fun getViewController(
        parent: ViewGroup,
        position: Int,
        dataType: Int
    ): ListPageBaseHeaderController<out BaseRenderer>? =
        when(dataType) {
            RendererType.HEADER_HOT_SEARCH-> HotSearchController(parent)
            else-> null
        }
}
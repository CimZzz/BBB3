package com.gogoh5.apps.quanmaomao.android.entities.creators

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.controllers.HomeController
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HomeHeadRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController

object HomeHeaderCreator: IListPageHeaderCreator {
    override fun getHeaderType(data: Any): Int =
        when(data) {
            is HomeHeadRenderer-> RendererType.HEADER_HOME
            else -> -1
        }

    override fun getViewController(
        parent: ViewGroup,
        position: Int,
        dataType: Int
    ): ListPageBaseHeaderController<out BaseRenderer>? =
        when(dataType) {
            RendererType.HEADER_HOME-> HomeController(parent)
            else-> null
        }
}
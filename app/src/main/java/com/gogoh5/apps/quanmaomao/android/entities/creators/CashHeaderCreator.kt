package com.gogoh5.apps.quanmaomao.android.entities.creators

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.controllers.CashController
import com.gogoh5.apps.quanmaomao.android.entities.renderers.CashHeadRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.entities.databeans.CashRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/2/25 18:10:37
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object CashHeaderCreator: IListPageHeaderCreator {
    override fun getHeaderType(data: Any): Int =
        when(data) {
            is CashHeadRenderer -> RendererType.HEADER_CASH
            else -> -1
        }

    override fun getViewController(
        parent: ViewGroup,
        position: Int,
        dataType: Int
    ): ListPageBaseHeaderController<out BaseRenderer>? =
        when(dataType) {
            RendererType.HEADER_CASH -> CashController(parent)
            else -> null
        }
}
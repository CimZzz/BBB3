package com.gogoh5.apps.quanmaomao.android.entities.creators

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.holders.BalanceHolder
import com.gogoh5.apps.quanmaomao.android.entities.holders.CashHolder
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.entities.databeans.BalanceRenderer
import com.gogoh5.apps.quanmaomao.library.entities.databeans.CashRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseContentHolder

object DetailContentCreator: IListPageContentCreator {

    override fun getContentType(data: Any): Int = when(data) {
        is BalanceRenderer -> RendererType.CONTENT_BALANCE
        is CashRenderer -> RendererType.CONTENT_CASH
        else-> -1
    }

    override fun getViewHolder(parent: ViewGroup, dataType: Int): ListPageBaseContentHolder<out BaseRenderer>? = when(dataType) {
        RendererType.CONTENT_BALANCE-> BalanceHolder(parent)
        RendererType.CONTENT_CASH-> CashHolder(parent)
        else-> null
    }
}
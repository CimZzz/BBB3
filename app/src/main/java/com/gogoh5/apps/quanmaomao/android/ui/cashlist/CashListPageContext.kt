package com.gogoh5.apps.quanmaomao.android.ui.cashlist

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.entities.creators.CashHeaderCreator
import com.gogoh5.apps.quanmaomao.android.entities.creators.DetailContentCreator
import com.gogoh5.apps.quanmaomao.android.entities.renderers.CashHeadRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.DefaultListPageContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.extensions.setMargin
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.requests.GetCashListRequest
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler

class CashListPageContext(context: Context): DefaultListPageContext(context) {
    override val isContentOnly: Boolean
        get() = true

    override val headerCreatorList: Array<IListPageHeaderCreator>
        get() = arrayOf(CashHeaderCreator)
    override val contentCreatorList: Array<IListPageContentCreator>
        get() = arrayOf(DetailContentCreator)

    override val defaultTopHeaderRenderer: Array<out BaseRenderer>?
        get() = arrayOf(CashHeadRenderer())


    override fun generateContentRequest(pageNum: Int, isInit: Boolean): BaseRequest<*>? =
        GetCashListRequest(SysContext.getUser().uid, pageNum)


    override fun onContentResult(pageNum: Int, isInit: Boolean, params: Array<out Any>): ListBean? {
        if(params[0] as Boolean)
            return params[1] as ListBean
        return null
    }

    override fun generateBottomStateBar(parent: ViewGroup): ViewHandler? {
        val handler = super.generateBottomStateBar(parent)
        handler?.setSize(height = SysContext.dp2px(70))
        handler?.viewVisibleCallback = {
            view, id, state ->
            when(id) {
                R.id.empty -> {
                    if(state == View.VISIBLE) {
                        view.setMargin(top = SysContext.dp2px(25))
                        (view as TextView).text = "您还没有任何提现记录，快去下单获得返利吧!"
                    }
                }
            }
        }
        return handler
    }
}
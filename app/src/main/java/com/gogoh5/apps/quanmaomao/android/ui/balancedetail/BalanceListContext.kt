package com.gogoh5.apps.quanmaomao.android.ui.balancedetail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.entities.creators.DetailContentCreator
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.DefaultListPageContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.requests.GetBalanceListRequest
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler

class BalanceListContext(context: Context): DefaultListPageContext(context) {
    override val isContentOnly: Boolean
        get() = true

    override val headerCreatorList: Array<IListPageHeaderCreator>
        get() = arrayOf()
    override val contentCreatorList: Array<IListPageContentCreator>
        get() = arrayOf(DetailContentCreator)

    override fun generateContentRequest(pageNum: Int, isInit: Boolean): BaseRequest<*>? =
        GetBalanceListRequest(SysContext.getUser().uid, pageNum)

    override fun onContentResult(pageNum: Int, isInit: Boolean, params: Array<out Any>): ListBean? {
        if(params[0] as Boolean)
            return params[1] as ListBean

        return null
    }

    override fun buildViewHandler(viewHandler: ViewHandler) {
        viewHandler.viewVisibleCallback = {
            view, id, state ->
            when(id) {
                R.id.empty -> {
                    if(state == View.VISIBLE) {
                        view.findViewById<TextView>(R.id.titleTxt).text = "您还没有任何余额明细记录，快去下单获得返利吧!"
                    }
                }
            }
        }
    }

    override fun generateBottomStateBar(parent: ViewGroup): ViewHandler? {
        val handler = super.generateBottomStateBar(parent)
        handler?.viewVisibleCallback = {
            view, id, state ->
            when(id) {
                R.id.empty -> {
                    if(state == View.VISIBLE) {
                        (view as TextView).text = "您还没有任何余额明细记录，快去下单获得返利吧!"
                    }
                }
            }
        }
        return handler
    }
}
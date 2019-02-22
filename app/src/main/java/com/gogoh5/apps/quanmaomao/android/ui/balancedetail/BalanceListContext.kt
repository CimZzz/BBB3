package com.gogoh5.apps.quanmaomao.android.ui.balancedetail

import android.content.Context
import com.gogoh5.apps.quanmaomao.android.entities.creators.DetailContentCreator
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.DefaultListPageContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.requests.GetBalanceListRequest

class BalanceListContext(context: Context): DefaultListPageContext(context) {
    override val isContentOnly: Boolean
        get() = true

    override val headerCreatorList: Array<IListPageHeaderCreator>
        get() = arrayOf()
    override val contentCreatorList: Array<IListPageContentCreator>
        get() = arrayOf(DetailContentCreator)

    override fun generateBrandListRequest(pageNum: Int, isInit: Boolean): BaseRequest<*> =
        GetBalanceListRequest(SysContext.getUser().uid, pageNum)

    override fun onContentResult(pageNum: Int, params: Array<out Any>): Pair<List<BaseRenderer>?, Boolean> {

        if(params[0] as Boolean) {
            val listBean = params[1] as ListBean
            return Pair(listBean.list as List<BaseRenderer>, listBean.over)
        }

        return Pair(null, false)
    }
}
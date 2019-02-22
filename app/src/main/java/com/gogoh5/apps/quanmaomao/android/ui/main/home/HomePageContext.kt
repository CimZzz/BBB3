package com.gogoh5.apps.quanmaomao.android.ui.main.home

import android.content.Context
import com.gogoh5.apps.quanmaomao.android.entities.creators.HomeHeaderCreator
import com.gogoh5.apps.quanmaomao.android.entities.creators.ProducHeaderCreator
import com.gogoh5.apps.quanmaomao.android.entities.pagecontexts.ProductListPageContext
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HomeHeadRenderer
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HotSearchRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.requests.GetProductListRequest

class HomePageContext(context: Context, val callback: Callback): ProductListPageContext(context) {
    val KEY_WORD = "_home_key_word"

    override val headerCreatorList: Array<IListPageHeaderCreator>
        get() = arrayOf(HomeHeaderCreator, ProducHeaderCreator)

    override val defaultHeaderRenderer: Array<out BaseRenderer>?
        get() = arrayOf(HomeHeadRenderer(), HotSearchRenderer())

    override val isContentOnly: Boolean
        get() = true


    override fun generateBrandListRequest(pageNum: Int, isInit: Boolean): BaseRequest<*> =
        GetProductListRequest(SysContext.getUser().uid, pageNum, getConfigValue(KEY_WORD))


    override fun onEvent(viewType: Int, vararg params: Any?) {
        when(viewType) {
            RendererType.HEADER_HOT_SEARCH -> {
                if(params.isNotEmpty()) {
                    if(params[0] is String) {
                        setConfigValue(KEY_WORD, params[0])
                        collapseHeader(true, false)
                        refreshContent()
                    }
                }
            }

            RendererType.HEADER_HOME -> {
                callback.transferToSearchPage()
            }
        }
    }




    interface Callback {
        fun transferToSearchPage()
    }
}
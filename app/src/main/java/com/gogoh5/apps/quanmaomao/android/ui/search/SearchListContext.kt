package com.gogoh5.apps.quanmaomao.android.ui.search

import android.content.Context
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.entities.filterbars.ProductFilterBar
import com.gogoh5.apps.quanmaomao.android.entities.pagecontexts.ProductListPageContext
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HomeHeadRenderer
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HotSearchRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController
import com.gogoh5.apps.quanmaomao.library.requests.GetProductListRequest

class SearchListContext(context: Context? = null): ProductListPageContext(context) {
    val KEY_WORD = "_home_key_word"

    override val isContentOnly: Boolean
        get() = true

    override val hasFilterBar: Boolean
        get() = true

    override val defaultHeaderRenderer: Array<out BaseRenderer>?
        get() = arrayOf(HotSearchRenderer())

    override fun generateFilterBarController(parent: ViewGroup): ListPageBaseHeaderController<*>? {
        return ProductFilterBar(parent)
    }

    override fun generateBrandListRequest(pageNum: Int, isInit: Boolean): BaseRequest<*> =
            GetProductListRequest(SysContext.getUser().uid,
                pageNum,
                order = if(isInit) 0 else getVariableValue(ProductFilterBar.KEY_ORDER)?:0,
                k = getConfigValue(KEY_WORD))

    fun configSearchContent(searchContent: String?) {
        setConfigValue(KEY_WORD, searchContent)
    }

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
        }
    }
}
package com.gogoh5.apps.quanmaomao.android.entities.pagecontexts

import android.content.Context
import com.gogoh5.apps.quanmaomao.android.entities.creators.ProducHeaderCreator
import com.gogoh5.apps.quanmaomao.android.entities.creators.ProductContentCreator
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductRenderer
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.DefaultListPageContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageContentCreator
import com.gogoh5.apps.quanmaomao.library.extended.listview.IListPageHeaderCreator
import com.gogoh5.apps.quanmaomao.library.requests.GetProductListRequest
import java.util.*

@Suppress("UNCHECKED_CAST")
abstract class ProductListPageContext(context: Context? = null): DefaultListPageContext(context) {

    override val headerCreatorList: Array<IListPageHeaderCreator>
        get() = arrayOf(ProducHeaderCreator)
    override val contentCreatorList: Array<IListPageContentCreator>
        get() = arrayOf(ProductContentCreator)


    override fun generateContentRequest(pageNum: Int, isInit: Boolean): BaseRequest<*>? = GetProductListRequest(SysContext.getUser().uid, pageNum)

    override fun onContentResult(pageNum: Int, isInit: Boolean, params: Array<out Any>): ListBean? {
        if (params[0] as Boolean)
            return params[1] as ListBean
        return null
    }


    override fun onContentFilter(rendererList: List<BaseRenderer>?): List<BaseRenderer>? {
        if(rendererList == null)
            return null

        val convertList = rendererList as List<ProductRenderer>
        val tempList = LinkedList<BaseRenderer>()
        convertList.filter {
            return@filter listPageDataBundle.checkAndAddFilter("${it.GoodsID}_${it.sourceType}")
        }.forEach {
            tempList.add(it)
        }

        return tempList
    }
}
package com.gogoh5.apps.quanmaomao.android.entities.pagecontexts

import android.content.Context
import com.gogoh5.apps.quanmaomao.android.entities.creators.ProducHeaderCreator
import com.gogoh5.apps.quanmaomao.android.entities.creators.ProductContentCreator
import com.gogoh5.apps.quanmaomao.android.entities.renderers.parseProductRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductBean
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

    override fun generateBrandListRequest(pageNum: Int, isInit: Boolean): BaseRequest<*> = GetProductListRequest(SysContext.getUser().uid, pageNum)

    override fun onContentResult(pageNum: Int, params: Array<out Any>): Pair<List<BaseRenderer>?, Boolean> {
        if (params[0] as Boolean) {
            val listBean = params[1] as ListBean

            val list = listBean.list as List<ProductBean>?

            if (list != null) {
                val tempList = LinkedList<BaseRenderer>()
                list.filter {
                    return@filter listPageDataBundle.checkAndAddFilter("${it.GoodsID}_${it.sourceType}")
                }.forEach {
                    val bean = parseProductRenderer(it)
                    if(bean != null)
                        tempList.add(bean)
                }

                return Pair(tempList, listBean.over)
            }

            return Pair(null, false)
        }

        return Pair(null, false)
    }
}
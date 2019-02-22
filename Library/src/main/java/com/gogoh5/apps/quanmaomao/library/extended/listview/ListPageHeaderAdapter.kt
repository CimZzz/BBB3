package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.toolkits.LazyLoadToolkit

class ListPageHeaderAdapter(private val listPageContext: ListPageContext<*>) : LazyLoadToolkit.Adapter() {
    override val itemCount: Int
        get() =
            if(listPageContext.hasFilterBar())
                listPageContext.listPageDataBundle.headerList.size + 1

            else listPageContext.listPageDataBundle.headerList.size

    internal fun notifyHeader() {
        notifyDataSetChanged()
    }

    internal fun notifyHeaderUpdateByPosition(position: Int) {
        notifyDataChangedAt(position)
    }


    override fun getData(position: Int): Any =
        if(listPageContext.hasFilterBar() && position == itemCount - 1)
            Unit
        else listPageContext.listPageDataBundle.headerList[position]

    override fun getItemDataType(data: Any, position: Int): Int {
        if(listPageContext.hasFilterBar() && position == itemCount - 1)
            return RendererType.HEADER_FILTER_BAR
        listPageContext.headerCreatorList.forEach {
            val itemType = it.getHeaderType(data)
            if(itemType != -1)
                return itemType
        }
        return -1
    }

    override fun createViewController(
        parent: ViewGroup,
        position: Int,
        dataType: Int
    ): LazyLoadToolkit.ViewController<*>? {
        if(dataType == RendererType.HEADER_FILTER_BAR) {
            val viewController = listPageContext.generateFilterBarController(parent)
            viewController?.callback = listPageContext.listPageViewCallback
            return viewController
        }
        listPageContext.headerCreatorList.forEach {
            val viewController = it.getViewController(parent, position, dataType)
            if(viewController != null) {
                viewController.callback = listPageContext.listPageViewCallback
                return viewController
            }
        }
        return null
    }
}
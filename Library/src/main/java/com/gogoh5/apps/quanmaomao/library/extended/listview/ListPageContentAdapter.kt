package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.R
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.constants.RendererType
import com.gogoh5.apps.quanmaomao.library.extensions.forEach
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler
import java.lang.RuntimeException

class ListPageContentAdapter(private val listPageContext: ListPageContext) : RecyclerView.Adapter<ListPageBaseContentHolder<out BaseRenderer>>() {
    private var isSlowly = true

    override fun getItemCount(): Int =
        if(listPageContext.isExistContentBottom)
            listPageContext.listPageDataBundle.contentList.size + 1
        else listPageContext.listPageDataBundle.contentList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        if(listPageContext.isExistContentBottom && position == listPageContext.listPageDataBundle.contentList.size)
            return RendererType.CONTENT_BOTTOM
        listPageContext.contentCreatorList.forEach {
            val itemType = it.getContentType(listPageContext.listPageDataBundle.contentList[position])
            if(itemType != -1)
                return itemType
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageBaseContentHolder<out BaseRenderer> {
        if(viewType == RendererType.CONTENT_BOTTOM)
            return BottomViewHolder(listPageContext.generateBottomStateBar(parent)?:throw RuntimeException("content bottom view is null"))

        listPageContext.contentCreatorList.forEach {
            val viewHolder = it.getViewHolder(parent, viewType)
            if(viewHolder != null) {
                viewHolder.callback = listPageContext.listPageViewCallback
                return viewHolder
            }
        }

        throw RuntimeException("Create null view holder")
    }

    override fun onBindViewHolder(holder: ListPageBaseContentHolder<out BaseRenderer>, position: Int) {
        if(holder.itemViewType == RendererType.CONTENT_BOTTOM)
            holder.bindBean()
        else holder.bindBean(listPageContext.listPageDataBundle.contentList[position])

        if(isSlowly)
            holder.slowlyShow()
        else holder.quickShow()
        listPageContext.checkPreload(position)
    }

    override fun onViewAttachedToWindow(holder: ListPageBaseContentHolder<out BaseRenderer>) {
        super.onViewAttachedToWindow(holder)
        holder.attachToList()
    }

    override fun onViewDetachedFromWindow(holder: ListPageBaseContentHolder<out BaseRenderer>) {
        super.onViewDetachedFromWindow(holder)
        holder.detachFromList()
    }

    internal fun doScrolling(list: RecyclerView) {
        if(isSlowly) {
            isSlowly = false
            list.forEach {
                val viewHolder = list.getChildViewHolder(it) as ListPageBaseContentHolder<*>
                viewHolder.quickShow()
            }
        }
    }

    internal fun doSlowScrolling(list: RecyclerView) {
        if(!isSlowly) {
            isSlowly = true
            list.forEach {
                val viewHolder = list.getChildViewHolder(it) as ListPageBaseContentHolder<*>
                viewHolder.slowlyShow()
            }
        }
    }

    inner class BottomViewHolder(private val viewHandler: ViewHandler) : ListPageBaseContentHolder<BaseRenderer>(viewHandler) {
        override fun bindBean() {
            when (listPageContext.listPageDataBundle.contentState) {
                ListPage.CONTENT_LOAD -> viewHandler.showView(R.id.loading)
                ListPage.CONTENT_OVER -> viewHandler.showView(R.id.over)
                ListPage.CONTENT_FAILED -> {
                    viewHandler.showView(R.id.error)?.tapWith {
                        viewHandler.showView(R.id.loading)
                        listPageContext.reloadPage()
                    }
                }
            }
        }
    }
}
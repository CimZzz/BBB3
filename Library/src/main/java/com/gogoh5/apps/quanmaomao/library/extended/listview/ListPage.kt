package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.R
import com.gogoh5.apps.quanmaomao.library.base.BasePage
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.library.widgets.LazyAppbarLayout
import com.gogoh5.apps.quanmaomao.library.widgets.ScrollCoordinateLayout
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler
import kotlinx.android.synthetic.main.sub_page_list.view.*
import java.lang.RuntimeException

@Suppress("UNCHECKED_CAST")
class ListPage<T: ListPageDataBundle>(private val listPageContext: ListPageContext<T>) : BasePage<T>(), IListPageView {

    companion object {
        internal val STATE_LOADING = 0
        internal val STATE_CONTENT = 1
        internal val STATE_FAILED = 2

        internal val CONTENT_LOAD = 0
        internal val CONTENT_FAILED = 2
        internal val CONTENT_OVER = 3

        internal val LAZY_MODE_NONE = 0
        internal val LAZY_MODE_UPDATE = 1

        internal const val VARIABLE_KEY_FILTER_BAR = "__Filter_Bar"
    }

    lateinit var viewHandler: ViewHandler
    lateinit var refreshHeaderView: View
    lateinit var contentView: ScrollCoordinateLayout
    lateinit var presenter: ListPagePresenter

    internal var filterWindow: ListPageFilterWindow? = null

    override fun generateDataParcelable(): T = listPageContext.generateDataBundle()

    override fun initViewPage(parent: ViewGroup, bindBean: Any, position: Int) {
        super.initViewPage(parent, bindBean, position)
        listPageContext.listPageView = this
        listPageContext.listPageDataBundle = bindBean as T
        presenter = ListPagePresenter(this)
        presenter.onInitPresenter(MixDataBundle(readable = {
            listPageContext
        }))

        listPageContext.listPagePresenter = presenter
    }


    override fun generateRootView(parent: ViewGroup): View {
        viewHandler = ViewUtils.inflateView(parent, R.layout.page_list)
        viewHandler.isSaveFromParentEnabled = false
        viewHandler.viewStubFirstBind = {
            view, id ->
            if(id == R.id.error) {
                view.tapWith {
                    presenter.refreshAll(true)
                }
            }
        }
        viewHandler.showView(R.id.loading)
        return viewHandler
    }


    override fun isViewFromObject(view: View): Boolean = view == rootView

    override fun attachView(parent: ViewGroup) {
        parent.addView(rootView)
    }

    override fun detachView(parent: ViewGroup) {
        parent.removeView(rootView)
    }

    override fun onEnterPage() {
        presenter.viewEnter(isFirstEnter)
    }

    override fun onQuitPage() {
        presenter.viewQuit()
    }

    override fun onDestroyPage() {
        presenter.viewDestroy()
    }

    override fun showLoading() {
        detachFilterWindow()
        viewHandler.showView(R.id.loading)
    }

    override fun showError() {
        detachFilterWindow()
        viewHandler.showView(R.id.error)
    }

    override fun showContent(data: Unit?) {
        detachFilterWindow()
        viewHandler.showView(R.id.content)
    }

    override fun initContent() {
        contentView = viewHandler.showView(R.id.content) as ScrollCoordinateLayout
        if(listPageContext.isAllowHeaderRefresh) {
            refreshHeaderView = listPageContext.generateRefreshHeader(contentView) ?: throw RuntimeException("refresh header is null")

            val layoutParams = refreshHeaderView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = ListPageRefreshViewBehavior()

            contentView.isAllowRefresh = true
            contentView.refreshListener = object : ScrollCoordinateLayout.OnRefreshListener {
                override fun getMaxScrollY(): Float = listPageContext.getRefreshMaxScrollY(refreshHeaderView)

                override fun onRefreshing() {
                    listPageContext.refreshAll(false)
                }

                override fun checkRefreshHeaderTarget(distance: Float): Float =
                    listPageContext.checkHeaderRefreshDistance(refreshHeaderView, distance)
            }
            contentView.addView(refreshHeaderView)
            listPageContext.generateOuterView(contentView, contentView.listHeader)?.let {
                contentView.addView(it)
            }

            if(listPageContext.isAutoHeaderOffset()) {
                contentView.listHeader.behavior.isAutoOffset = true
                contentView.listContent.setSize(height = ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
        else contentView.isAllowRefresh = false

        viewHandler.listHeader.setAdapter(listPageContext.headerAdapter)

        listPageContext.buildContentList(viewHandler.listContent)
        viewHandler.listContent.adapter = listPageContext.contentAdapter
        viewHandler.listContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if(recyclerView?.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if(Math.abs(dy) < 20)
                        listPageContext.contentAdapter.doSlowScrolling(viewHandler.listContent)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                when(newState) {
                    RecyclerView.SCROLL_STATE_SETTLING -> listPageContext.contentAdapter.doScrolling(viewHandler.listContent)
                    RecyclerView.SCROLL_STATE_DRAGGING -> listPageContext.contentAdapter.doSlowScrolling(viewHandler.listContent)
                    else -> listPageContext.contentAdapter.doSlowScrolling(viewHandler.listContent)
                }
            }
        })
    }

    override fun collapseHeader(collapseHeader: Boolean, animate: Boolean) {
        viewHandler.listContent.stopScroll()
        viewHandler.listHeader.stopScroll()
        viewHandler.listHeader.setExpanded(!collapseHeader, animate)
    }

    override fun completeRefresh() {
        if(listPageContext.isAllowHeaderRefresh)
            contentView.completed()
    }

    override fun attachFilterWindow()  {
        if(filterWindow == null) {
            filterWindow = listPageContext.generateFilterWindow(viewHandler)
            filterWindow?.callback = listPageContext.listPageViewCallback
            filterWindow?.initView(viewHandler)
        }

        filterWindow?.updateView()
        viewHandler.addView(filterWindow?.rootView)
    }

    override fun detachFilterWindow()  {
        viewHandler.removeView(filterWindow?.rootView ?: return)
    }


    fun attachRootView(view: View) = viewHandler.addView(view)


    fun showView(id: Int) = viewHandler.showView(id)

    fun refresh() {
        presenter.refreshAll(true)
    }
}


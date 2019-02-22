package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.widgets.ScrollCoordinateLayout
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler
import java.lang.ref.WeakReference

abstract class ListPageContext<T : ListPageDataBundle>(context: Context? = null) {
    lateinit var listPageDataBundle: T
        internal set
    internal lateinit var listPageView: IListPageView
    internal lateinit var listPagePresenter: ListPagePresenter

    private val contextRef: WeakReference<Context?> = WeakReference(context)

    val headerAdapter: ListPageHeaderAdapter by lazy { ListPageHeaderAdapter(this) }
    val contentAdapter: ListPageContentAdapter by lazy { ListPageContentAdapter(this) }
    internal val listPageViewCallback: ListPageViewCallback by lazy { ListPageViewCallback(this) }
    internal var isContentLoading : Boolean = false

    open val isHeaderOnly: Boolean = false
    open val isContentOnly: Boolean = false
    protected open val isAutoHeaderOffset: Boolean = false
    abstract val isDependentContentRequest: Boolean
    abstract val isExistContentBottom: Boolean
    abstract val isAllowHeaderRefresh: Boolean
    protected abstract val hasFilterBar: Boolean
    abstract val headerCreatorList: Array<IListPageHeaderCreator>
    abstract val contentCreatorList: Array<IListPageContentCreator>
    open val defaultHeaderRenderer: Array<out BaseRenderer>? = null

    abstract fun generateDataBundle() : T

    open fun buildContentList(recyclerView: RecyclerView) {

    }

    open fun generateOuterView(parent: ScrollCoordinateLayout, headerParent: AppBarLayout): View? = null

    open fun generateRefreshHeader(parent: ViewGroup): View? = null

    open fun generateBottomStateBar(parent: ViewGroup) : ViewHandler? = null


    open fun generateFilterBarController(parent: ViewGroup): ListPageBaseHeaderController<*>? = null

    open fun generateFilterWindow(parent: ViewGroup): ListPageFilterWindow? = null
    open fun onLinkInterceptor(viewType: Int, link: BaseLink?) {
    }

    open fun onEvent(viewType: Int, vararg params: Any?) {

    }

    open fun checkHeaderRefreshDistance(headerView: View, distance: Float): Float = 0f

    open fun getRefreshMaxScrollY(headerView: View): Float = -1f

    internal fun isAutoHeaderOffset() = !isContentOnly && (isAutoHeaderOffset || isHeaderOnly)

    internal fun hasFilterBar() = hasFilterBar && !isHeaderOnly

    internal fun checkPreload(position: Int) {
        if(listPageDataBundle.contentState == ListPage.CONTENT_LOAD &&
            !isContentLoading && onCheckPreload(position, listPageDataBundle.contentList.size)) {
            isContentLoading = true
            listPagePresenter.requestPage(listPageDataBundle.pageNum + 1)
        }
    }

    fun attachFilterWindow() {
        if(listPageDataBundle.state == ListPage.STATE_CONTENT) {
            collapseHeader(true, false)
            listPageView.attachFilterWindow()
        }
    }

    fun detachFilterWindow() {
        if(listPageDataBundle.state == ListPage.STATE_CONTENT) {
            listPageView.detachFilterWindow()
        }
    }

    fun refreshAll(isForce: Boolean = true) {
        listPagePresenter.refreshAll(isForce)
    }

    fun refreshContent() {
        listPagePresenter.requestPage(0)
    }

    fun reloadPage() {
        listPagePresenter.requestPage(listPageDataBundle.pageNum + 1)
    }

    fun collapseHeader(collapseHeader: Boolean, animate: Boolean) {
        if(listPageDataBundle.state == ListPage.STATE_CONTENT)
            listPageView.collapseHeader(collapseHeader, animate)
    }

    fun getContext(): Context? = contextRef.get()

    inline fun <reified T> getVariableValue(key: String, defaultValue: T? = null): T? = listPageDataBundle.getVariable(key, defaultValue) as T
    fun setVariableValue(key: String, any: Any?) = listPageDataBundle.setVariable(key, any)

    inline fun <reified T> getConfigValue(key: String, defaultValue: T? = null): T? = listPageDataBundle.getConfig(key, defaultValue) as T
    fun setConfigValue(key: String, any: Any?) = listPageDataBundle.setConfig(key, any)

    abstract fun generateBrandRequest() : BaseRequest<*>
    abstract fun generateBrandListRequest(pageNum: Int, isInit: Boolean): BaseRequest<*>
    abstract fun onHeaderResult(params: Array<out Any>): List<BaseRenderer>?
    abstract fun onContentResult(pageNum: Int, params: Array<out Any>): Pair<List<BaseRenderer>?, Boolean>
    abstract fun onCheckPreload(position: Int, totalCount: Int) : Boolean
}
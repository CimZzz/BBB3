package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean
import com.gogoh5.apps.quanmaomao.library.widgets.ScrollCoordinateLayout
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler
import java.lang.ref.WeakReference

abstract class ListPageContext(context: Context? = null) {
    lateinit var listPageDataBundle: ListPageDataBundle
        internal set

    private val contextRef: WeakReference<Context?> = WeakReference(context)
    internal lateinit var listPageView: IListPageView
    internal lateinit var listPagePresenter: ListPagePresenter
    internal val headerAdapter: ListPageHeaderAdapter by lazy { ListPageHeaderAdapter(this) }
    internal val contentAdapter: ListPageContentAdapter by lazy { ListPageContentAdapter(this) }
    internal val listPageViewCallback: ListPageViewCallback by lazy { ListPageViewCallback(this) }

    /*配置参数*/
    abstract val headerCreatorList: Array<IListPageHeaderCreator>
    abstract val contentCreatorList: Array<IListPageContentCreator>
    open val defaultTopHeaderRenderer: Array<out BaseRenderer>? = null
    open val defaultBottomHeaderRenderer: Array<out BaseRenderer>? = null


    /*配置开关*/
    open val isHeaderOnly: Boolean = false
    open val isContentOnly: Boolean = false
    open val isUnionRequest: Boolean = false
    abstract val isDependentContentRequest: Boolean
    abstract val isExistContentBottom: Boolean
    abstract val isExistFilterBar: Boolean
    abstract val isAllowHeaderRefresh: Boolean
    open val isAutoHeaderOffset: Boolean = false

    /*临时状态参数*/

    /*生命周期*/
    abstract fun generateDataBundle() : ListPageDataBundle

    open fun buildViewHandler(viewHandler: ViewHandler) {

    }

    open fun buildContentList(recyclerView: RecyclerView) {

    }

    /*生成相关View*/
    open fun generateRefreshHeader(parent: ViewGroup): View? = null

    open fun generateBottomStateBar(parent: ViewGroup) : ViewHandler? = null

    open fun generateFilterBarController(parent: ViewGroup): ListPageBaseHeaderController<*>? = null

    open fun generateFilterWindow(parent: ViewGroup): ListPageFilterWindow? = null

    open fun generateOuterView(parent: ScrollCoordinateLayout, headerParent: AppBarLayout): View? = null


    /*事件拦截*/
    open fun onLinkInterceptor(viewType: Int, link: BaseLink?) {
    }

    open fun onEvent(viewType: Int, vararg params: Any?) {

    }

    /*检查方法*/
    open fun checkHeaderRefreshDistance(headerView: View, distance: Float): Float = 0f

    open fun checkRefreshMaxScrollY(headerView: View): Float = -1f

    internal fun checkPreload(position: Int) {
        if(listPageDataBundle.contentState == ListPage.CONTENT_LOAD &&
            !listPageDataBundle.isContentLoading && onCheckPreload(position, listPageDataBundle.contentList.size)) {
            listPagePresenter.requestPage(listPageDataBundle.pageNum + 1)
        }
    }

    abstract fun onCheckPreload(position: Int, totalCount: Int) : Boolean

    /*请求方法*/
    abstract fun generateHeaderRequest(): BaseRequest<*>?
    abstract fun onHeaderResult(params: Array<out Any>): List<BaseRenderer>?

    abstract fun generateContentRequest(pageNum: Int, isInit: Boolean): BaseRequest<*>?
    abstract fun onContentResult(pageNum: Int, isInit: Boolean, params: Array<out Any>): ListBean?
    open fun onContentFilter(rendererList: List<BaseRenderer>?): List<BaseRenderer>? = rendererList

    /*包装方法*/

    internal fun isAutoHeaderOffset() = !isContentOnly && (isAutoHeaderOffset || isHeaderOnly)

    internal fun hasFilterBar() = isExistFilterBar && !isHeaderOnly

    internal fun reloadPage() {
        listPagePresenter.requestPage(listPageDataBundle.pageNum + 1)
    }

    fun getContext(): Context? = contextRef.get()

    fun collapseHeader(collapseHeader: Boolean, animate: Boolean) {
        if(listPagePresenter.markDone)
            listPageView.collapseHeader(collapseHeader, animate)
    }

    fun attachFilterWindow() {
        if(listPagePresenter.markDone)
            collapseHeader(true, false)
            listPageView.attachFilterWindow()
    }

    fun detachFilterWindow() {
        if(listPagePresenter.markDone)
            listPageView.detachFilterWindow()
    }

    fun refreshContent() {
        listPagePresenter.requestPage(0)
    }

    fun refreshAll(isForce: Boolean = true) {
        listPagePresenter.refreshAll(isForce)
    }

    inline fun <reified T> getVariableValue(key: String, defaultValue: T? = null): T? = listPageDataBundle.getVariable(key, defaultValue) as T
    fun setVariableValue(key: String, any: Any?) = listPageDataBundle.setVariable(key, any)

    inline fun <reified T> getConfigValue(key: String, defaultValue: T? = null): T? = listPageDataBundle.getConfig(key, defaultValue) as T
    fun setConfigValue(key: String, any: Any?) = listPageDataBundle.setConfig(key, any)
}
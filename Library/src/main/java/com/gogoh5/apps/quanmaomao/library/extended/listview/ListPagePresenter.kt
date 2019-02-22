package com.gogoh5.apps.quanmaomao.library.extended.listview

import com.gogoh5.apps.quanmaomao.library.base.BasePresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncCode
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncRule
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncRuleGroup
import okhttp3.Call
import java.lang.ref.WeakReference

class ListPagePresenter(view: IListPageView) : BasePresenter<IListPageView>(view) {
    private val weakRef = WeakReference(this)
    private lateinit var listPageContext: ListPageContext<*>
    private var lazyLoadMode: Int = ListPage.LAZY_MODE_NONE

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        listPageContext = mixDataBundle.getObject("")!!
    }

    override fun onViewSaveInstance(mixDataBundle: MixDataBundle) = Unit

    override fun onViewRestoreInstance(mixDataBundle: MixDataBundle) = Unit

    override fun onViewEnter() {
        if(viewFirstEnter) {
            if(listPageContext.listPageDataBundle.state != ListPage.STATE_CONTENT) {
                refreshAll(true)
            }
            else lazyLoad(true)
        }
        else lazyLoad()
    }

    override fun onViewQuit() {

    }

    override fun onViewDestroy() {
        weakRef.clear()
        allCall?.cancel()
        allCode.finish()
        pageCall?.cancel()
        pageCode.finish()
    }

    private fun lazyLoad(lazyLoad: Boolean = false) {
        if(viewEnter) {
            if(lazyLoadMode == ListPage.LAZY_MODE_UPDATE || lazyLoad) {
                when (listPageContext.listPageDataBundle.state) {
                    ListPage.STATE_LOADING -> view.showLoading()
                    ListPage.STATE_FAILED -> view.showError()
                    ListPage.STATE_CONTENT -> {
                        if (markDone) {
                            view.showContent()
                            listPageContext.headerAdapter.notifyDataSetChanged()
                            listPageContext.contentAdapter.notifyDataSetChanged()
                            view.completeRefresh()
                        } else {
                            markDoneAction()
                            view.showContent()
                            view.initContent()
                        }
                    }
                }
                lazyLoadMode = ListPage.LAZY_MODE_NONE
            }
        }
        else if(lazyLoad)
            lazyLoadMode = ListPage.LAZY_MODE_UPDATE
    }

    //RefreshAll
    private val allCode = SyncCode()
    private var allCall: Call? = null
    private var contentCall: Call? = null
    private var isForceAll: Boolean = false
    private var isHeaderCompleted: Boolean = false
    private var isContentCompleted: Boolean = false
    private var contentState: Int = ListPage.CONTENT_LOAD
    private var tempHeaderList: List<BaseRenderer>? = null
    private var tempContentList: List<BaseRenderer>? = null
    internal fun refreshAll(isForceAll: Boolean) {
        this.isForceAll = isForceAll
        allCall?.cancel()
        contentCall?.cancel()
        allCode.nextCode()
        isHeaderCompleted = false
        isContentCompleted = false
        contentState = ListPage.CONTENT_LOAD
        tempHeaderList = null
        tempContentList = null

        if(isForceAll) {
            allCode.recodeCode()
            listPageContext.listPageDataBundle.resetAllData()
            if(listPageContext.defaultHeaderRenderer != null)
                listPageContext.listPageDataBundle.headerList.addAll(listPageContext.defaultHeaderRenderer!!)
            listPageContext.isContentLoading = false
            listPageContext.listPageDataBundle.state = ListPage.STATE_LOADING
            lazyLoad(true)
        }

        if(!listPageContext.isContentOnly) {
            if(listPageContext.isHeaderOnly) {
                isContentCompleted = true
                contentState = ListPage.CONTENT_OVER
            }

            val request = listPageContext.generateBrandRequest()
                .dataHunter(RefDataHunter(
                    weakRef,
                    isMainThread = true,
                    syncRuleGroup = SyncRuleGroup(arrayOf(SyncRule(allCode.code, allCode)))
                ) {
                    presenter, params ->

                    val listPageContext = presenter.listPageContext
                    val list = listPageContext.onHeaderResult(params)

                    if(list != null) {
                        //Success
                        presenter.allCode.recodeCode()
                        presenter.isHeaderCompleted = true
                        presenter.tempHeaderList = list
                        if (!listPageContext.isHeaderOnly && !listPageContext.isDependentContentRequest)
                            presenter.requestContent()
                        else presenter.checkAllCompleted()
                    }
                    else {
                        //Failure
                        presenter.contentCall?.cancel()
                        if (presenter.isForceAll) {
                            presenter.allCode.recodeCode()
                            listPageContext.listPageDataBundle.resetAllData()
                            if(listPageContext.defaultHeaderRenderer != null)
                                listPageContext.listPageDataBundle.headerList.addAll(listPageContext.defaultHeaderRenderer!!)
                            listPageContext.isContentLoading = false
                            listPageContext.listPageDataBundle.state = ListPage.STATE_FAILED
                            presenter.lazyLoad(true)
                        }
                    }
                })
            allCall = SysContext.getHttp().enqueue(request)
        }

        else {
            allCode.recodeCode()
            isHeaderCompleted = true
        }

        if(!listPageContext.isHeaderOnly && listPageContext.isDependentContentRequest)
                requestContent()
    }


    //Request Content
    private fun requestContent() {
        val request = listPageContext.generateBrandListRequest(0, true)
            .dataHunter(RefDataHunter(
                weakRef,
                isMainThread = true,
                syncRuleGroup = SyncRuleGroup(arrayOf(SyncRule(allCode.code, allCode)))
            ) {
                presenter, params->

                val listPageContext = presenter.listPageContext

                val pair = listPageContext.onContentResult(0, params)
                presenter.isContentCompleted = true
                presenter.tempContentList = pair.first
                when {
                    pair.first == null -> {
                        presenter.contentState = ListPage.CONTENT_FAILED
                        if (listPageContext.isContentOnly) {
                            presenter.allCode.recodeCode()
                            listPageContext.listPageDataBundle.resetAllData()
                            if(listPageContext.defaultHeaderRenderer != null)
                                listPageContext.listPageDataBundle.headerList.addAll(listPageContext.defaultHeaderRenderer!!)
                            listPageContext.isContentLoading = false
                            listPageContext.listPageDataBundle.state = ListPage.STATE_FAILED
                            presenter.lazyLoad(true)
                            return@RefDataHunter
                        }
                    }
                    pair.second -> presenter.contentState = ListPage.CONTENT_OVER
                    else -> presenter.contentState = ListPage.CONTENT_LOAD
                }
                presenter.checkAllCompleted()
            })

        contentCall = SysContext.getHttp().enqueue(request)
    }


    private fun checkAllCompleted() {
        if(isHeaderCompleted && isContentCompleted) {
            val dataBundle = listPageContext.listPageDataBundle
            dataBundle.resetAllData()
            if(listPageContext.defaultHeaderRenderer != null)
                dataBundle.headerList.addAll(listPageContext.defaultHeaderRenderer!!)
            dataBundle.headerList.addAll(tempHeaderList?: emptyList())
            dataBundle.contentList.addAll(tempContentList?: emptyList())
            if(contentState == ListPage.CONTENT_FAILED)
                dataBundle.pageNum = -1
            else dataBundle.pageNum = 0
            dataBundle.contentState = contentState
            dataBundle.state = ListPage.STATE_CONTENT
            listPageContext.isContentLoading = false
            lazyLoad(true)
        }
    }


    //RefreshPage
    private val pageCode = SyncCode()
    private var pageCall: Call? = null
    private var pageNum: Int = 0
    internal fun requestPage(pageNum: Int) {
        pageCall?.cancel()
        pageCode.nextCode()
        pageCode.recodeCode(allCode.code)
        this.pageNum = pageNum

        this.listPageContext.isContentLoading = true
        if(pageNum == 0) {
            listPageContext.listPageDataBundle.resetPageData()
            lazyLoad(true)
        }

        val request = listPageContext.generateBrandListRequest(pageNum, false)
            .dataHunter(RefDataHunter(
                weakRef,
                isMainThread = true,
                syncRuleGroup = SyncRuleGroup(
                    arrayOf(SyncRule(pageCode.code, pageCode), SyncRule(pageCode.recordCode, allCode))
                )
            ) {
                presenter, params->

                val listPageContext = presenter.listPageContext
                val pair = listPageContext.onContentResult(presenter.pageNum, params)
                val tempContentList = pair.first
                val contentState : Int
                val dataBundle = listPageContext.listPageDataBundle
                contentState = when {
                    pair.first == null -> ListPage.CONTENT_FAILED
                    pair.second -> {
                        dataBundle.pageNum = presenter.pageNum
                        ListPage.CONTENT_OVER
                    }
                    else -> {
                        dataBundle.pageNum = presenter.pageNum
                        ListPage.CONTENT_LOAD
                    }
                }

                dataBundle.contentList.addAll(tempContentList?: emptyList())
                dataBundle.contentState = contentState
                listPageContext.isContentLoading = false
                presenter.lazyLoad(true)
            })

        pageCall = SysContext.getHttp().enqueue(request)
    }
}
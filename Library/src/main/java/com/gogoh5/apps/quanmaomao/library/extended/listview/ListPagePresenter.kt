package com.gogoh5.apps.quanmaomao.library.extended.listview

import com.gogoh5.apps.quanmaomao.library.base.*
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ListBean

class ListPagePresenter(view: IListPageView, val listPageContext: ListPageContext) : BaseMethodPresenter<IListPageView, ListPageMethod>(view) {

    override fun generateMethod(): ListPageMethod = ListPageMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
    }

    override fun onViewEnter() {
        if(viewFirstEnter)
            refreshAll(true)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onLazyLoad() {
        //刷新全部
        val headerLazyBean = getLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER)
        val contentLazyBean = getLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT)
        if(headerLazyBean != null && contentLazyBean != null) {
            if(headerLazyBean.isSuccess() && (!listPageContext.isUnionRequest || contentLazyBean.isSuccess())) {
                listPageContext.listPageDataBundle.resetAllData()
                val headerRenderer = headerLazyBean.obj as? List<BaseRenderer>?
                val defaultTop = listPageContext.defaultTopHeaderRenderer
                val defaultBottom = listPageContext.defaultBottomHeaderRenderer
                if(defaultTop != null)
                    listPageContext.listPageDataBundle.headerList.addAll(defaultTop)
                if(headerRenderer != null)
                    listPageContext.listPageDataBundle.headerList.addAll(headerRenderer)
                if(defaultBottom != null)
                    listPageContext.listPageDataBundle.headerList.addAll(defaultBottom)

                if(contentLazyBean != BaseLazyLoadBean.EMPTY) {
                    if(contentLazyBean.isSuccess()) {
                        val listBean = contentLazyBean.obj as ListBean
                        val contentList = listPageContext.onContentFilter(listBean.list)
                        if (contentList != null)
                            listPageContext.listPageDataBundle.contentList.addAll(contentList)

                        if (listBean.over) {
                            if(listPageContext.listPageDataBundle.contentList.isEmpty())
                                listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_EMPTY
                            else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_OVER
                        }
                        else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_LOAD
                        listPageContext.listPageDataBundle.pageNum = 0
                    }
                    else {
                        listPageContext.listPageDataBundle.pageNum = -1
                        listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_FAILED
                    }
                }
                else {
                    listPageContext.listPageDataBundle.pageNum = 0
                    if(listPageContext.listPageDataBundle.contentList.isEmpty())
                        listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_EMPTY
                    else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_OVER
                }


                if(listPageContext.listPageDataBundle.headerList.isEmpty() &&
                    listPageContext.listPageDataBundle.contentList.isEmpty() &&
                    listPageContext.listPageDataBundle.contentState == ListPage.CONTENT_EMPTY) {
                    view.showEmpty()
                }
                else {
                    if(markDone) {
                        listPageContext.headerAdapter.notifyDataSetChanged()
                        listPageContext.contentAdapter.notifyDataSetChanged()
                        view.completeRefresh()
                    }
                    else view.showContent()
                }

                markDoneAction()
            }
            else view.showError()

            method.recordAllCode()

            consumeAllLazyLoadParams()
            return
        }

        if(markDone) {
            val clearLazyBean = getLazyLoadParams(ListPage.LAZY_LOAD_CLEAR_PAGE)
            if(clearLazyBean != null) {
                listPageContext.listPageDataBundle.resetPageData()
                listPageContext.contentAdapter.notifyDataSetChanged()
                consumeLazyLoadParams(ListPage.LAZY_LOAD_CLEAR_PAGE)
            }

            val pageLazyBean = getLazyLoadParams(ListPage.LAZY_LOAD_PAGE)
            if(pageLazyBean != null) {
                if(contentLazyBean != BaseLazyLoadBean.EMPTY) {
                    if (pageLazyBean.isSuccess()) {
                        val listBean = pageLazyBean.obj as ListBean
                        val contentList = listPageContext.onContentFilter(listBean.list)
                        if (contentList != null)
                            listPageContext.listPageDataBundle.contentList.addAll(contentList)

                        if (listBean.over) {
                            if(listPageContext.listPageDataBundle.contentList.isEmpty())
                                listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_EMPTY
                            else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_OVER
                        }
                        else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_LOAD
                        listPageContext.listPageDataBundle.pageNum = listBean.pageNum
                    } else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_FAILED
                }
                else {
                    if(listPageContext.listPageDataBundle.contentList.isEmpty())
                        listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_EMPTY
                    else listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_OVER
                }
            }

            if(listPageContext.listPageDataBundle.headerList.isEmpty() &&
                listPageContext.listPageDataBundle.contentList.isEmpty() &&
                listPageContext.listPageDataBundle.contentState == ListPage.CONTENT_EMPTY) {
                view.showEmpty()
            }
            else listPageContext.contentAdapter.notifyDataSetChanged()
            consumeLazyLoadParams(ListPage.LAZY_LOAD_PAGE)
        }
    }

    fun refreshAll(isForceAll: Boolean) {
        consumeLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER)
        consumeLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT)
        if(isForceAll) {
            markDoneAction(false)
            listPageContext.listPageDataBundle.resetAllData()
            method.resetAllRequest()
            consumeAllLazyLoadParams()
        }
        method.nextAllCode()

        if(listPageContext.isHeaderOnly) {
            val headRequest = listPageContext.generateHeaderRequest()
            if(headRequest == null) {
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean.EMPTY)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean.EMPTY)
                return
            }
            method.handleHeadRequest(headRequest)
        }
        else if(listPageContext.isContentOnly) {
            val contentRequest = listPageContext.generateContentRequest(0, true)
            if(contentRequest == null) {
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean.EMPTY)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean.EMPTY)
                return
            }
            method.handleContentRequest(request = contentRequest)
        }
        else {
            val headRequest = listPageContext.generateHeaderRequest()
            if(headRequest == null)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean.EMPTY)
            else method.handleHeadRequest(headRequest)

            if(headRequest == null || listPageContext.isDependentContentRequest) {
                val contentRequest = listPageContext.generateContentRequest(0, true)
                if(contentRequest == null)
                    putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean.EMPTY)
                else method.handleContentRequest(request = contentRequest)
            }
        }
    }

    internal fun onHeaderCallback(code: Int, params: Array<out Any>) {
        val list = listPageContext.onHeaderResult(params)

        if(!listPageContext.isDependentContentRequest && !listPageContext.isHeaderOnly) {
            val contentRequest = listPageContext.generateContentRequest(0, true)
            if(contentRequest == null)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean.EMPTY)
            else method.handleContentRequest(code, contentRequest)
        }


        if(list != null) {
            if(listPageContext.isHeaderOnly)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean.EMPTY)
            putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean(list).buildSuccess())
        }
        else {
            if(listPageContext.isHeaderOnly)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean().buildFailure())
            putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean().buildFailure())
        }
    }

    internal fun onAllContentCallback(params: Array<out Any>) {
        val listBean = listPageContext.onContentResult(0, true, params)

        if(listBean != null) {
            if(listPageContext.isContentOnly)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean.EMPTY)
            putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean(listBean).buildSuccess())
        }
        else {
            if(listPageContext.isContentOnly)
                putLazyLoadParams(ListPage.LAZY_LOAD_ALL_HEADER, BaseLazyLoadBean().buildFailure())
            putLazyLoadParams(ListPage.LAZY_LOAD_ALL_CONTENT, BaseLazyLoadBean().buildFailure())
        }
    }


    fun requestPage(pageNum: Int) {
        consumeLazyLoadParams(ListPage.LAZY_LOAD_CLEAR_PAGE)
        method.resetPageRequest()
        if(pageNum == 0)
            putLazyLoadParams(ListPage.LAZY_LOAD_CLEAR_PAGE, BaseLazyLoadBean.EMPTY)

        listPageContext.listPageDataBundle.contentState = ListPage.CONTENT_LOADING

        val request = listPageContext.generateContentRequest(pageNum, false)
        if(request == null) {
            putLazyLoadParams(ListPage.LAZY_LOAD_PAGE, BaseLazyLoadBean.EMPTY)
            return
        }
        request.otherParams(pageNum)
        method.handlePageRequest(request)
    }

    internal fun onPageCallback(pageNum: Int, params: Array<out Any>) {
        val listBean = listPageContext.onContentResult(pageNum, false, params)
        if(listBean != null)
            putLazyLoadParams(ListPage.LAZY_LOAD_PAGE, BaseLazyLoadBean(listBean).buildSuccess())
        else putLazyLoadParams(ListPage.LAZY_LOAD_PAGE, BaseLazyLoadBean().buildFailure())
    }
}
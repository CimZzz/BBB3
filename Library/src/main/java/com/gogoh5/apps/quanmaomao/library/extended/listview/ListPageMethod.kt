package com.gogoh5.apps.quanmaomao.library.extended.listview

import com.gogoh5.apps.quanmaomao.library.base.BaseMethod
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncCode
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncRule
import com.gogoh5.apps.quanmaomao.library.toolkits.syncGroupOf

class ListPageMethod(presenter: ListPagePresenter) : BaseMethod<ListPagePresenter>(presenter) {
    val allCode: SyncCode = SyncCode()
    val pageCode: SyncCode = SyncCode()

    override fun onDestroy() {
        super.onDestroy()
        allCode.nextCode()
        allCode.recodeCode()
        pageCode.nextCode()
    }

    fun nextAllCode() {
        allCode.nextCode()
    }

    fun recordAllCode() {
        allCode.recodeCode()
    }

    fun handleHeadRequest(request: BaseRequest<*>) {
        val code = allCode.code
        request.dataHunter(RefDataHunter(
            presenterRef,
            syncRuleGroup = syncGroupOf(SyncRule(code, allCode))
        ) {
            presenter, params ->
            presenter.onHeaderCallback(params[2] as Int, params)
        }).otherParams(code)
        appendRequest(0, request)
    }

    fun handleContentRequest(code: Int? = null, request: BaseRequest<*>) {
        request.dataHunter(RefDataHunter(
            presenterRef,
            syncRuleGroup = syncGroupOf(SyncRule(code?:allCode.code, allCode))
        ) {
            presenter, params ->
            presenter.onAllContentCallback(params)
        })
        appendRequest(1, request)

    }

    fun handlePageRequest(request: BaseRequest<*>) {
        request.dataHunter(RefDataHunter(
            presenterRef,
            syncRuleGroup = syncGroupOf(
                SyncRule(allCode.recordCode, allCode, SyncRule.TYPE_REOCRD_CODE),
                SyncRule(pageCode.nextCode(), pageCode)
            )
        ) {
            presenter, params ->
            presenter.onPageCallback(params[2] as Int, params)
        })

        appendRequest(2, request)
    }


    fun resetAllRequest() {
        allCode.nextCode()
        allCode.recodeCode()
        pageCode.nextCode()
        stopCall(0)
        stopCall(1)
        stopCall(2)
        removeCall(0)
        removeCall(1)
        removeCall(2)
    }

    fun resetPageRequest() {
        pageCode.nextCode()
        stopCall(2)
        removeCall(2)
    }
}
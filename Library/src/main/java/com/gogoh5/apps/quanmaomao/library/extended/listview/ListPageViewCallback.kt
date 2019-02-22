package com.gogoh5.apps.quanmaomao.library.extended.listview

import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import java.lang.ref.WeakReference

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/21 00:32:41
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class ListPageViewCallback(listPageContext: ListPageContext<*>, private val prefix: String = "") {

    private val contentRef = WeakReference(listPageContext)

    fun getContext() = contentRef.get()

    fun getPrefix() = prefix

    fun onClick(viewType: Int, baseLink: BaseLink? = null) {
        getContext()?.onLinkInterceptor(viewType, baseLink)
    }

    fun onEvent(viewType: Int, vararg params: Any?) {
        getContext()?.onEvent(viewType, *params)
    }

    fun refreshContent() {
        getContext()?.refreshContent()
    }

    fun collapseHeader(collapse: Boolean, animate: Boolean = false) {
        getContext()?.collapseHeader(collapse, animate)
    }

    fun showFilterWindow() {
        getContext()?.attachFilterWindow()
    }

    fun hideFilterWindow() {
        getContext()?.detachFilterWindow()
    }


    inline fun <reified T> getVariableValue(key: String, defaultValue: T? = null): T? {
        return getContext()?.listPageDataBundle?.getVariable(key, defaultValue) as T
    }

    fun setVariableValue(key: String, any: Any?) = getContext()?.setVariableValue(key, any)


    inline fun <reified T> getConfigValue(key: String, defaultValue: T? = null): T? {
        return getContext()?.listPageDataBundle?.getConfig(key, defaultValue) as T
    }

    fun setConfigValue(key: String, any: Any?) = getContext()?.setConfigValue(key, any)
}
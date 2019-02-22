@file:Suppress("UNCHECKED_CAST")

package com.gogoh5.apps.quanmaomao.library.base

import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup

abstract class BasePage<T : Any> {

    /**
     * Judge first enter
     */
    protected var isFirstEnter = true
        private set

    /**
     * Judge view enter
     */
    var viewEnter = false
        private set

    /**
     * root view
     */
    lateinit var rootView: View
        private set

    /**
     * position
     */
    var position: Int = 0

    /**
     * data bean
     */
    lateinit var pageBean: T
        private set


    /**
     * Init page root view
     */
    @CallSuper
    open fun initViewPage(parent: ViewGroup, bindBean : Any, position: Int) {
        this.pageBean = bindBean as T
        this.position = position
        this.rootView = generateRootView(parent)
    }

    /**
     * Generate data parcelable
     */
    abstract fun generateDataParcelable() : T

    /**
     * Generate root view
     */
    abstract fun generateRootView(parent: ViewGroup) : View

    /**
     * Judge view is from this page
     */
    abstract fun isViewFromObject(view: View): Boolean

    /**
     * Attach to view pager
     */
    abstract fun attachView(parent: ViewGroup)

    /**
     * Detach from view pager
     */
    abstract fun detachView(parent: ViewGroup)


    /**
     * Page enter
     */
    fun enterPage() {
        viewEnter = true
        onEnterPage()
        if(isFirstEnter)
            isFirstEnter = false
    }

    /**
     * Page enter implementation
     */
    protected abstract fun onEnterPage()

    /**
     * Page changed
     */
    open fun onChanged(vararg params: Any) {
    }

    /**
     * Page quit
     */
    fun quitPage() {
        viewEnter = false
        onQuitPage()
    }

    /**
     * Page quit implementation
     */
    protected abstract fun onQuitPage()


    /**
     * Page destroy
     */
    fun destroyPage() {
        if(viewEnter) {
            viewEnter = false
            onQuitPage()
        }
        onDestroyPage()
    }

    /**
     * Page destroy implementation
     */
    protected abstract fun onDestroyPage()


}
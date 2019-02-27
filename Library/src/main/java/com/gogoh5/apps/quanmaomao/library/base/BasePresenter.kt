package com.gogoh5.apps.quanmaomao.library.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import com.gogoh5.apps.quanmaomao.library.extensions.SpArray
import com.gogoh5.apps.quanmaomao.library.utils.ToastUtils

abstract class BasePresenter<T : IView>(val view: T) {
    var viewEnter : Boolean = false
        private set

    var viewFirstEnter : Boolean = false
        private set

    var markDone : Boolean = false
        private set

    private var lazyLoadMap: SpArray<BaseLazyLoadBean?>? = null


    /**
     * Initialize presenter from out side
     */
    fun initPresenter(savedInstanceState: Bundle?, intent: Intent?) {
        if(this == EMPTY)
            return

        onInitPresenter(MixDataBundle(savedInstanceState, intent))
    }

    /**
     * Initialize presenter implementation
     */
    abstract fun onInitPresenter(mixDataBundle: MixDataBundle)


    /**
     * Save presenter data from out side
     */
    fun viewSaveInstance(outState: Bundle?) {
        if(this == EMPTY)
            return

        onViewSaveInstance(MixDataBundle(outState))
    }

    /**
     * Save presenter data implementation
     */
    open fun onViewSaveInstance(mixDataBundle: MixDataBundle) {}


    /**
     * Restore presenter data from out side
     */
    fun viewRestoreInstance(outState: Bundle?) {
        if(this == EMPTY)
            return

        onViewRestoreInstance(MixDataBundle(outState))
    }

    /**
     * Restore presenter data implementation
     */
    open fun onViewRestoreInstance(mixDataBundle: MixDataBundle) {}

    /**
     * View appear
     * @param isFirstEnter when view first appear, is true
     */
    fun viewEnter(isFirstEnter : Boolean) {
        if(!this.viewEnter) {
            this.viewFirstEnter = isFirstEnter
            this.viewEnter = true
            onViewEnter()
            if(lazyLoadMap != null)
                onLazyLoad()
        }
    }

    /**
     * View appear implementation
     */
    open fun onViewEnter() {}

    /**
     * View lazy load
     */
    protected open fun onLazyLoad() {

    }


    /**
     * View disappear
     */
    fun viewQuit() {
        if(this.viewEnter) {
            this.viewEnter = false
            onViewQuit()
        }
    }

    /**
     * View disappear implementation
     */
    open fun onViewQuit() {}


    /**
     * Process new intent
     */
    open fun newIntent(intent: Intent?) {
    }

    /**
     * View destroy
     */
    @CallSuper
    open fun viewDestroy() {
        onViewDestroy()
        lazyLoadMap?.clear()
    }

    /**
     * View destroy implementation
     */
    open fun onViewDestroy() {}

    /**
     * Mark action done
     */
    fun markDoneAction(isDone: Boolean = true) {
        markDone = isDone
    }

    /**
     * Put lazy load params
     * If viewEnter == true , will run
     */
    fun putLazyLoadParams(position: Int, params: BaseLazyLoadBean?) {
        var lazyLoadMap = this.lazyLoadMap
        if(lazyLoadMap == null) {
            lazyLoadMap = SpArray()
            this.lazyLoadMap = lazyLoadMap
        }

        lazyLoadMap.put(position, params)

        if(viewEnter)
            onLazyLoad()
    }

    /**
     * Get lazy load params
     */
    protected fun getLazyLoadParams(position: Int): BaseLazyLoadBean? = lazyLoadMap?.get(position)

    /**
     * Consume lazy load params
     */
    protected fun consumeLazyLoadParams(position: Int) {
        this.lazyLoadMap?.remove(position)
    }

    /**
     * Consume all lazy load params
     */
    protected fun consumeAllLazyLoadParams() {
        this.lazyLoadMap?.clear()
    }


    /*Quick methods*/

    /**
     * Send toast
     */
    fun sendToast(toast: Any?) {
        ToastUtils.sendToast(toast)
    }



    companion object {
        val EMPTY : BasePresenter<IView> = object : BasePresenter<IView>(IView.EMPTY) {
            override fun onViewEnter() {

            }

            override fun onViewQuit() {
            }

            override fun onInitPresenter(mixDataBundle: MixDataBundle) {
            }

            override fun onViewSaveInstance(mixDataBundle: MixDataBundle) {
            }

            override fun onViewRestoreInstance(mixDataBundle: MixDataBundle) {
            }

            override fun onViewDestroy() {
            }

        }
    }
}
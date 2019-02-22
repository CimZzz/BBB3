package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils

@Suppress("UNCHECKED_CAST")
abstract class ListPageBaseContentHolder<T: BaseRenderer> : RecyclerView.ViewHolder {
    constructor(itemView: View?) : super(itemView)
    constructor(parent: ViewGroup, layoutId: Int) : super(ViewUtils.inflateView(parent, layoutId))

    var isSlowlyShow: Boolean = false
        private set

    lateinit var callback: ListPageViewCallback
        internal set

    lateinit var bean: T
        private set


    internal fun bindBean(bean: BaseRenderer) {
        this.bean = bean as T
        bindBean()
    }

    abstract fun bindBean()


    @CallSuper
    open fun slowlyShow() {
        isSlowlyShow = true
    }

    @CallSuper
    open fun quickShow() {
        isSlowlyShow = false
    }

    open fun attachToList() {

    }

    open fun detachFromList() {

    }
}
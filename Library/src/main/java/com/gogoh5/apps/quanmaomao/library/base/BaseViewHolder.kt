package com.gogoh5.apps.quanmaomao.library.base

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T: Any>(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    lateinit var bean: T
        private set

    fun bindBean(bean: T) {
        this.bean = bean
        onBind()
    }

    abstract fun onBind()
}
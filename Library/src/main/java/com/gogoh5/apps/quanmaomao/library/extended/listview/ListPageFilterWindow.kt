package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.view.View
import android.view.ViewGroup

abstract class ListPageFilterWindow {
    lateinit var callback: ListPageViewCallback
        internal set
    lateinit var rootView: View
        internal set

    internal fun initView(parent: ViewGroup) {
        rootView = generateView(parent)
        onInitView(parent)
    }

    internal fun updateView() {
        onUpdateView()
    }


    abstract fun generateView(parent: ViewGroup): View
    abstract fun onInitView(parent: ViewGroup)
    abstract fun onUpdateView()
}

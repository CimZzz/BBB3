package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.toolkits.LazyLoadToolkit

abstract class ListPageBaseHeaderController<T: BaseRenderer>(parent: ViewGroup) :
    LazyLoadToolkit.ViewController<T>(parent) {

    lateinit var callback: ListPageViewCallback
        internal set
}
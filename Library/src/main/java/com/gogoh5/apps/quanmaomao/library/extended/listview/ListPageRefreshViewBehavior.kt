package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/20 02:45:40
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class ListPageRefreshViewBehavior: CoordinatorLayout.Behavior<View> {
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        if(child != null && dependency != null)
            child.translationY = (-child.height + dependency.top).toFloat()
        return super.onDependentViewChanged(parent, child, dependency)
    }

}
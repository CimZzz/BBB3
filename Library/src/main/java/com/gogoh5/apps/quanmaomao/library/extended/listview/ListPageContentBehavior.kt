package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.gogoh5.apps.quanmaomao.library.R

class ListPageContentBehavior: AppBarLayout.ScrollingViewBehavior {
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return dependency?.id == R.id.listHeader
    }
}
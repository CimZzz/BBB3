package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.gogoh5.apps.quanmaomao.library.toolkits.LazyLoadToolkit

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/13 01:26:14
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class LazyLinearLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val lazyLoadToolkit = LazyLoadToolkit(this)

    init {
        orientation = VERTICAL
    }

    fun setAdapter(adapter : LazyLoadToolkit.Adapter?) = lazyLoadToolkit.setAdapter(adapter)

    fun onVerticalOffsetChanged(offset: Int, viewOffset: Int = 0) = lazyLoadToolkit.onVerticalOffsetChanged(offset, viewOffset)
    fun onVerticalOffsetChanged(offset: Int, viewOffset: Int = 0, verticalRange : Int) = lazyLoadToolkit.onVerticalOffsetChanged(offset, viewOffset, verticalRange)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        lazyLoadToolkit.onLayoutChanged()
    }
}
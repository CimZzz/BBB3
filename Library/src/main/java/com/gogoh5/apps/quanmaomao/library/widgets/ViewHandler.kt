package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewStub

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/08 13:07:56
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class ViewHandler : ConstraintLayout {
    var currentShowId: Int = View.NO_ID
        private set
    var currentShowView: View? = null

    var isSquare: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    var viewStubFirstBind : ((View, Int) -> Unit)? = null
    var viewVisibleCallback : ((View, Int, Int) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec
        if(isSquare) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)

            if(width == 0 && height != 0)
                widthSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(widthMeasureSpec))
            else if(width != 0 && height == 0)
                heightSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(heightMeasureSpec))
        }

        super.onMeasure(widthSpec, heightSpec)
    }

    fun showView(layoutId : Int) : View? {
        if(currentShowId == layoutId)
            return currentShowView
        var view : View
        (0 until childCount).map { getChildAt(it) }.filter { it.id != View.NO_ID && !(it.tag?.equals("No")?:false) }.forEach {

            if(it.id == layoutId) {
                view = it
                if(it is ViewStub)
                    view = it.inflate()
                it.visibility = View.VISIBLE
                viewStubFirstBind?.let {
                    runnable ->
                    runnable(view, layoutId)
                }

                viewVisibleCallback?.let {
                    runnable->
                    runnable(view, layoutId, View.VISIBLE)
                }
                currentShowView = view
                currentShowId = layoutId
            }
            else {
                if(it.tag == "EXIST") {
                    it.visibility = View.INVISIBLE
                    viewVisibleCallback?.let {
                        runnable->
                        runnable(it, layoutId, View.INVISIBLE)
                    }
                }
                else {
                    it.visibility = View.GONE
                    viewVisibleCallback?.let {
                        runnable->
                        runnable(it, layoutId, View.GONE)
                    }
                }


            }
        }

        return currentShowView
    }


}
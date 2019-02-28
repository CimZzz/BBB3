package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller

class DraggableView : FrameLayout {
    private val TIME_SLOP: Long = 100
    private var lastMotionX: Float = 0f
    private var lastMotionY: Float = 0f

    private var startX: Float = 0f
    private var startY: Float = 0f

    private var endX: Float = 0f
    private var endY: Float = 0f

    private var maximumX: Float = 0f
    private var maximumY: Float = 0f

    private val scroller: Scroller

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        endX = -1f
        endY = -1f
        scroller = Scroller(context, AccelerateDecelerateInterpolator())
    }

    fun reset() {
        scroller.abortAnimation()
        endX = -1f
        endY = -1f
        layout(0, 0, 0, 0)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            endX = scroller.currX.toFloat()
            endY = scroller.currY.toFloat()
            layout(endX.toInt(), endY.toInt(), endX.toInt() + width, endY.toInt() + height)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val arr = IntArray(2, {0})
        getLocationInWindow(arr)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scroller.abortAnimation()
                lastMotionX = event.rawX
                lastMotionY = event.rawY
                startX = left.toFloat()
                startY = top.toFloat()

                val viewParent = parent
                if (viewParent != null && viewParent is View) {
                    val view = viewParent as View
                    maximumX = (view.width - width).toFloat()
                    maximumY = (view.height - height).toFloat()
                } else {
                    maximumX = 0f
                    maximumY = 0f
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.eventTime - event.downTime < TIME_SLOP)
                    return true
                val diffX = event.rawX - lastMotionX
                val diffY = event.rawY - lastMotionY

                var nextX = startX + diffX
                var nextY = startY + diffY

                if (nextX < 0)
                    nextX = 0f
                else if (nextX > maximumX)
                    nextX = maximumX

                if (nextY < 0)
                    nextY = 0f
                else if (nextY > maximumY)
                    nextY = maximumY

                endX = nextX
                endY = nextY
                layout(endX.toInt(), endY.toInt(), endX.toInt() + width, endY.toInt() + height)
            }
            MotionEvent.ACTION_UP -> {
                if (event.eventTime - event.downTime < TIME_SLOP) {
                    performClick()
                    return true
                }

                val diffX = event.rawX - lastMotionX
                val diffY = event.rawY - lastMotionY

                var nextX = startX + diffX
                var nextY = startY + diffY
                val oldX = nextX

                if (nextX > maximumX / 2)
                    nextX = maximumX
                else
                    nextX = 0f

                if (nextY < 0)
                    nextY = 0f
                else if (nextY > maximumY)
                    nextY = maximumY


                scroller.startScroll(oldX.toInt(), nextY.toInt(), (nextX - oldX).toInt(), 0)
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }
        return true
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (endX == -1f && endY == -1f)
            super.onLayout(changed, left, top, right, bottom)
        else {
            if (left.toFloat() != endX || top.toFloat() != endY) {
                layout(endX.toInt(), endY.toInt(), endX.toInt() + measuredWidth, endY.toInt() + measuredHeight)
                return
            }
            super.onLayout(
                changed,
                endX.toInt(),
                endY.toInt(),
                endX.toInt() + measuredWidth,
                endY.toInt() + measuredHeight
            )
        }
    }
}

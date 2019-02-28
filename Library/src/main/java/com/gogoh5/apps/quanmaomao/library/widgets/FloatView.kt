package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class FloatView: FrameLayout {
    private val TIME_SLOP: Long = 100
    private var isTouching: Boolean = false

    private var lastMotionX: Float = 0f
    private var lastMotionY: Float = 0f

    private var startX: Float = 0f
    private var startY: Float = 0f

    private var marginX: Float = 0f
    private var marginY: Float = 0f

    private var endX: Float = 0f
    private var endY: Float = 0f

    private var maximumX: Float = 0f
    private var maximumY: Float = 0f

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager


    private val scroller: Scroller = Scroller(context, AccelerateDecelerateInterpolator())

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            moveTo(scroller.currX, scroller.currY)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scroller.abortAnimation()
                isTouching = true
                lastMotionX = event.rawX
                lastMotionY = event.rawY
                val params = layoutParams as WindowManager.LayoutParams
                startX = params.x.toFloat()
                startY = params.y.toFloat()
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.eventTime - event.downTime < TIME_SLOP)
                    return true
                val diffX = event.rawX - lastMotionX
                val diffY = event.rawY - lastMotionY

                moveTo((startX + diffX).toInt(), (startY + diffY).toInt())
            }
            MotionEvent.ACTION_UP -> {
                isTouching = false
                if (event.eventTime - event.downTime < TIME_SLOP) {
                    performClick()
                    return true
                }

                val diffX = event.rawX - lastMotionX
                val diffY = event.rawY - lastMotionY

                var nextX = startX + diffX
                var nextY = startY + diffY

                val screenWidth = SysContext.getScreenWidth()
                val screenHeight = SysContext.getScreenHeight()
                if(nextX > screenWidth / 2)
                    nextX = screenWidth - marginX
                else nextX = marginX

                if(nextY < marginY)
                    nextY = marginY
                else if(nextY > screenHeight - marginY)
                    nextY = screenHeight - marginY

                scrollTo(nextX, nextY)
            }
        }
        return true
    }

    fun moveTo(x: Int, y: Int) {
        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        layoutParams.x = x
        layoutParams.y = y
        windowManager.updateViewLayout(this, layoutParams)
    }

    fun scrollTo(x: Float, y: Float) {
        if(isTouching)
            return
        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        scroller.startScroll(layoutParams.x, layoutParams.y, (x - layoutParams.x).toInt(), (y - layoutParams.y).toInt())
        ViewCompat.postInvalidateOnAnimation(this)
    }

}
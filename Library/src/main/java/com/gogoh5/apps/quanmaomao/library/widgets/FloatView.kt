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
import com.gogoh5.apps.quanmaomao.library.extensions.Run

class FloatView: FrameLayout {
    private val TIME_SLOP: Long = 100
    private var isTouching: Boolean = false

    private var lastMotionX: Float = 0f
    private var lastMotionY: Float = 0f

    private var startX: Float = 0f
    private var startY: Float = 0f

    private var marginX: Float = 0f
    private var marginY: Float = 0f

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val scroller: Scroller = Scroller(context, AccelerateDecelerateInterpolator())

    var callback: Callback? = null
    private var scrollOverRun: Run? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            if(scroller.isFinished)
                scrollOverRun?.let { it() }
            moveTo(scroller.currX, scroller.currY)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                forceStop()
                callback?.onTouched()
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
                    nextX = screenWidth - width - marginX
                else nextX = marginX

                if(nextY < marginY)
                    nextY = marginY
                else if(nextY > screenHeight - height - marginY)
                    nextY = screenHeight - height - marginY

                scrollTo(nextX, nextY)
            }
        }
        return true
    }

    fun forceStop() {
        scroller.abortAnimation()
        this.scrollOverRun = null
    }

    fun moveTo(x: Int, y: Int) {
        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        layoutParams.x = x
        layoutParams.y = y
        windowManager.updateViewLayout(this, layoutParams)
    }

    fun scrollToCenter(callback: Run? = null) {
        val screenWidth = SysContext.getScreenWidth()
        val screenHeight = SysContext.getScreenHeight()
        val endX = screenWidth / 2 - width / 2
        val endY = screenHeight / 2 - height / 2


        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        scroller.startScroll(layoutParams.x, layoutParams.y, (endX - layoutParams.x).toInt(), (endY - layoutParams.y).toInt())
        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun scrollToRightBottom(offsetX: Float? = null, offsetY: Float? = null, callback: Run? = null) {
        if(isTouching)
            return

        this.scrollOverRun = callback
        val screenWidth = SysContext.getScreenWidth()
        val screenHeight = SysContext.getScreenHeight()
        var endX = screenWidth - width - marginX - (offsetX?:0f)
        var endY = screenHeight - height - marginY - (offsetY?:0f)

        if(endX > screenWidth / 2)
            endX = screenWidth - width - marginX
        else endX = marginX

        if(endY < marginY)
            endY = marginY
        else if(endY > screenHeight - height - marginY)
            endY = screenHeight - height - marginY

        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        scroller.startScroll(layoutParams.x, layoutParams.y, (endX - layoutParams.x).toInt(), (endY - layoutParams.y).toInt())
        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun scrollTo(x: Float, y: Float) {
        if(isTouching)
            return
        val layoutParams = this.layoutParams as WindowManager.LayoutParams
        scroller.startScroll(layoutParams.x, layoutParams.y, (x - layoutParams.x).toInt(), (y - layoutParams.y).toInt())
        ViewCompat.postInvalidateOnAnimation(this)
    }


    interface Callback {
        fun onTouched()
    }
}
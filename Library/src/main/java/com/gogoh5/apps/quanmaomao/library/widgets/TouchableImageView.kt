package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewConfigurationCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.OverScroller
import android.widget.Scroller
import com.gogoh5.apps.quanmaomao.library.extensions.getLengthOfPoint
import com.gogoh5.apps.quanmaomao.library.extensions.lengthOf
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz

class TouchableImageView: AppCompatImageView {
    companion object {
        val SCALE_RATIO = 1f / 400f
        val MAX_SCALE_RATIO = 3f
        val MIN_SCALE_RATIO = 0.5f

        val ANIM_SCALE = 0
        val ANIM_NORMAL_MOVE = 1
    }

    private var viewWidth: Int = -1
    private var viewHeight: Int = -1

    private var isInit: Boolean = false
    private var orgRatio: Float = 1f
    private var orgOffsetX: Float = 0f
    private var orgOffsetY: Float = 0f
    private var afterBitmapWidth: Float = 0f
    private var afterBitmapHeight: Float = 0f
    private val customMatrix = Matrix()
    private val scroller = OverScroller(context)
    private var type: Int = 0

    private var curMoveX: Float = 0f
    private var curMoveY: Float = 0f
    private var curScaleRatio: Float = 1f

    private var moveRatio: Float = 1f
    private val movePoint = PointF()
    private val pointOne = PointF()
    private val pointTwo = PointF()
    private val moveTwoPoint = PointF()
    private var betweenLength: Float = 0.0f

    private val pointThree = PointF()

    private var onePointOnly: Boolean = false


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun drawableStateChanged() {
        isInit = false
        super.drawableStateChanged()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scroller.abortAnimation()
                initPointOne(event)
                onePointOnly = true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                onePointOnly = false
                when(event.pointerCount) {
                    2 -> initPointTwo(event)
                    3 -> initPointThree(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when(event.pointerCount) {
                    1 -> {
                        val distanceX = event.x - pointOne.x
                        val distanceY = event.y - pointOne.y

                        movePoint.x = curMoveX + distanceX
                        movePoint.y = curMoveY + distanceY

                        postInvalidate()
                    }
                    2 -> {
                        val curOneX = event.getX(0)
                        val curOneY = event.getY(0)


                        val curTwoX = event.getX(1)
                        val curTwoY = event.getY(1)

                        val currentLength = getLengthOfPoint(curOneX, curTwoX, curOneY, curTwoY)
                        val lengthChangedAmount = currentLength - betweenLength

                        val changeRatio = lengthChangedAmount * SCALE_RATIO

                        val lastRatio = moveRatio
                        moveRatio = curScaleRatio + changeRatio
                        if(moveRatio <= MIN_SCALE_RATIO)
                            moveRatio = MIN_SCALE_RATIO
                        else if(moveRatio >= MAX_SCALE_RATIO)
                            moveRatio = MAX_SCALE_RATIO



                        val realChangeRatio = moveRatio - lastRatio
                        if(moveRatio < 1f) {
                            val afterWidth = afterBitmapWidth * moveRatio
                            val afterHeight = afterBitmapHeight * moveRatio
                            movePoint.x = (afterBitmapWidth - afterWidth) * 1f / 2
                            movePoint.y = (afterBitmapHeight - afterHeight) * 1f / 2
                        }
                        else {
//                            val beforeWidth = lastRatio * afterBitmapWidth
//                            val beforeHeight = lastRatio * afterBitmapHeight
//
//
//                            var beforeIndicateWidth = centerX - movePoint.x
//                            var beforeIndicateHeight = centerY - movePoint.y
//
//                            if(beforeIndicateWidth <= 0f)
//                                beforeIndicateWidth = 0f
//                            else if(beforeIndicateWidth >= beforeWidth)
//                                beforeIndicateWidth = beforeWidth
//
//
//                            if(beforeIndicateHeight <= 0f)
//                                beforeIndicateHeight = 0f
//                            else if(beforeIndicateHeight >= beforeHeight)
//                                beforeIndicateHeight = beforeHeight
//
//
//                            val afterIndicateWidth = beforeIndicateWidth * moveRatio
//                            val afterIndicateHeight = beforeIndicateHeight * moveRatio
//
//                            movePoint.x -= beforeIndicateWidth * realChangeRatio
//                            movePoint.y -= beforeIndicateHeight * realChangeRatio

                            val changeWidth = realChangeRatio * afterBitmapWidth
                            val changeHeight = realChangeRatio * afterBitmapHeight

                            val centerX = (curOneX + curTwoX) / 2
                            val centerY = (curOneY + curTwoY) / 2

                            val xRatio = centerX / viewWidth
                            val yRatio = centerY / viewWidth

                            movePoint.x -= xRatio * changeWidth
                            movePoint.y -= yRatio * changeHeight
                        }

                        postInvalidate()
                    }

                    3 -> {

                    }
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                when(event.pointerCount) {
                    2 -> {
                        curMoveX = movePoint.x
                        curMoveY = movePoint.y
                        curScaleRatio = moveRatio
                        initPointOne(event)
                    }
                    3 -> initPointTwo(event)
                    4 -> initPointThree(event)
                }
            }

            MotionEvent.ACTION_UP -> {
                if(onePointOnly && event.eventTime - event.downTime < 200) {
                    performClick()
                }
                curMoveX = movePoint.x
                curMoveY = movePoint.y
                curScaleRatio = moveRatio

                val afterWidth = afterBitmapWidth * curScaleRatio
                val afterHeight = afterBitmapHeight * curScaleRatio

                if(curScaleRatio < 1f) {
                    type = ANIM_SCALE
                    val startX = (curScaleRatio * 100).toInt()
                    val endX = 100 - startX
                    scroller.startScroll(startX, 0, endX, 0)
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                else if(curScaleRatio == 1f) {
                    type = ANIM_NORMAL_MOVE
                    scroller.startScroll(curMoveX.toInt(), curMoveY.toInt(), (-curMoveX).toInt(), (-curMoveY).toInt())
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                else {
                    type = ANIM_NORMAL_MOVE
                    val slopWidth = afterWidth - viewWidth
                    val slopHeight = afterHeight - viewHeight

                    val afterX = orgOffsetX + curMoveX
                    val afterY = orgOffsetY + curMoveY

                    var dx = 0f
                    var dy = 0f

                    if(slopWidth < 0)
                        dx = (afterBitmapWidth - afterWidth) * 1f / 2 - curMoveX
                    else {
                         if(afterX > 0)
                            dx = 0 - afterX
                        else if(afterX < -slopWidth)
                            dx = -slopWidth - afterX
                    }

                    if(slopHeight < 0)
                        dy = (afterBitmapHeight - afterHeight) * 1f / 2 - curMoveY
                    else {
                        if(afterY > 0)
                            dy = 0 - afterY
                        else if(afterY < -slopHeight)
                            dy = -slopHeight - afterY
                    }

                    scroller.startScroll(curMoveX.toInt(), curMoveY.toInt(), (dx).toInt(), (dy).toInt())
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                postInvalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        if(scroller.computeScrollOffset() && !scroller.isFinished) {
            when(type) {
                ANIM_SCALE -> {
                    moveRatio = scroller.currX * 1f / 100
                    curScaleRatio = moveRatio
                    val afterWidth = afterBitmapWidth * moveRatio
                    val afterHeight = afterBitmapHeight * moveRatio
                    movePoint.x = (afterBitmapWidth - afterWidth) * 1f / 2
                    movePoint.y = (afterBitmapHeight - afterHeight) * 1f / 2
                    curMoveX = movePoint.x
                    curMoveY = movePoint.y
                }
                ANIM_NORMAL_MOVE -> {
                    movePoint.x = scroller.currX.toFloat()
                    movePoint.y = scroller.currY.toFloat()
                    curMoveX = movePoint.x
                    curMoveY = movePoint.y
                }
            }

            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun initPointOne(event: MotionEvent) {
        if(event.pointerCount == 2) {
            if(event.actionIndex == 0) {
                pointOne.x = event.getX(1)
                pointOne.y = event.getY(1)
            }
            else {
                pointOne.x = event.getX(0)
                pointOne.y = event.getY(0)
            }
        }
        else {
            pointOne.x = event.x
            pointOne.y = event.y
        }
    }

    private fun initPointTwo(event: MotionEvent) {
        if(event.pointerCount == 3) {
            var indexCount = 0

            if(indexCount == event.actionIndex)
                indexCount ++
            pointOne.x = event.getX(indexCount)
            pointOne.y = event.getY(indexCount)
            indexCount ++

            if(indexCount == event.actionIndex)
                indexCount ++

            pointTwo.x = event.getX(indexCount)
            pointTwo.y = event.getY(indexCount)
        }
        else {
            pointOne.x = event.getX(0)
            pointOne.y = event.getY(0)

            pointTwo.x = event.getX(1)
            pointTwo.y = event.getY(1)
        }

        moveTwoPoint.x = movePoint.x
        moveTwoPoint.y = movePoint.y

        moveRatio = curScaleRatio
        betweenLength = pointOne.lengthOf(pointTwo)
    }

    private fun initPointThree(event: MotionEvent) {
        pointOne.x = event.getX(0)
        pointOne.y = event.getY(0)

        pointTwo.x = event.getX(1)
        pointTwo.y = event.getY(1)

        pointThree.x = event.getX(2)
        pointThree.y = event.getY(2)
    }


    override fun onDraw(canvas: Canvas?) {
        if(drawable == null || canvas == null) {
            super.onDraw(canvas)
            return
        }
        canvas.drawARGB(0,0,0,0)

        if(drawable is BitmapDrawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap

            if(viewWidth != width || viewHeight != height)
                isInit = false


            if(!isInit) {
                isInit = true
                viewWidth = width
                viewHeight = height
                val wRatio = width * 1f / bitmap.width
                val hRatio = height * 1f / bitmap.height

                orgRatio = Math.min(wRatio, hRatio)

                afterBitmapWidth = bitmap.width * orgRatio
                afterBitmapHeight = bitmap.height * orgRatio

                orgOffsetX = (viewWidth - afterBitmapWidth) * 1f / 2
                orgOffsetY = (viewHeight - afterBitmapHeight) * 1f / 2
            }

            val ratio = orgRatio * moveRatio

            customMatrix.setScale(ratio, ratio)
            customMatrix.postTranslate(orgOffsetX + movePoint.x, orgOffsetY + movePoint.y)
            canvas.drawBitmap(bitmap, customMatrix, null)
        }

    }
}
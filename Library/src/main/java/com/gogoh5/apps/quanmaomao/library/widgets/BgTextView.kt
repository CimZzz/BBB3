package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.library.R

class BgTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {
    private var bitmap: Drawable? = null
    private val srcRect = Rect()
    private val destRect = Rect()
    init {
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.BgTextView)
        if(typeArray != null) {
            bitmap = typeArray.getDrawable(R.styleable.BgTextView_src)
            typeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas != null) {
            bitmap?.let {
                if (it is BitmapDrawable) {
                    val bitmap = it.bitmap

                    val orgRatio = height * 1f / bitmap.height
                    val leftPoint = (paddingLeft / orgRatio).toInt()
                    val rightPoint = (bitmap.width - paddingRight / orgRatio).toInt()


                    srcRect.set(0, 0, leftPoint, bitmap.height)
                    destRect.set(0, 0, paddingLeft, height)

                    canvas.drawBitmap(bitmap, srcRect, destRect, null)

                    srcRect.set(leftPoint, 0, rightPoint, bitmap.height)
                    destRect.set(paddingLeft, 0, width - paddingRight, height)

                    canvas.drawBitmap(bitmap, srcRect, destRect, null)

                    srcRect.set(rightPoint, 0, bitmap.width, bitmap.height)
                    destRect.set(width - paddingRight, 0, width, height)

                    canvas.drawBitmap(bitmap, srcRect, destRect, null)
                }
            }
        }
        super.onDraw(canvas)
    }
}
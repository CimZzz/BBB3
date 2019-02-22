package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.gogoh5.apps.quanmaomao.library.R

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/30 15:54:43
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class BgImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    val selfMatrix: Matrix = Matrix()

    var gravity: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BgImageView)
        gravity = typedArray.getInt(R.styleable.BgImageView_gravity, 0)
        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        if(drawable == null || canvas == null) {
            return
        }

        val bitmapW = (drawable as BitmapDrawable).bitmap.width
        val bitmapH = (drawable as BitmapDrawable).bitmap.height
        val ratio: Float

        if(bitmapW > width && bitmapH > height) {
            val wRatio = width * 1f / bitmapW
            val hRatio = height * 1f / bitmapH

            ratio = Math.max(wRatio, hRatio)
        }
        else if(bitmapW == width || bitmapH == height)
            ratio = 1f
        else {
            val wRatio = width * 1f / bitmapW
            val hRatio = height * 1f / bitmapH

            ratio= Math.max(wRatio, hRatio)
        }

        val afterW = bitmapW * ratio
        val afterH = bitmapH * ratio

//        LogUtils.cimzzz("width : $width , height : $height")
//        LogUtils.cimzzz("afterW : $afterW , afterH : $afterH")

        selfMatrix.setScale(ratio, ratio)
        if(gravity == 0)
            selfMatrix.postTranslate((width - afterW) / 2, height - afterH)
        else selfMatrix.postTranslate((width - afterW) / 2, 0f)

        canvas.drawBitmap((drawable as BitmapDrawable).bitmap, selfMatrix, null)
    }
}
package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.RectF
import com.gogoh5.apps.quanmaomao.library.R


class RoundImageView: ImageView {
    val path = Path()
    val rect = RectF()
    var radius: Float = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typeArr = context?.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        if(typeArr != null) {
            radius = typeArr.getDimension(R.styleable.RoundImageView_radius, 0f)
        }

        typeArr?.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)//设置可显示的区域，canvas四个角会被剪裁掉
        super.onDraw(canvas)
    }
}
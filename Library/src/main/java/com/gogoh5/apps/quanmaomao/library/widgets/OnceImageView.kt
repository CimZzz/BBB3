package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class OnceImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    private var recordWidth: Int = 0
    private var recordHeight: Int = 0
    var isLoading: Boolean = false
    var isSuccess: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (recordWidth != 0 && recordHeight != 0)
            super.onMeasure(
                View.MeasureSpec.makeMeasureSpec(recordWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(recordHeight, View.MeasureSpec.EXACTLY)
            )
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun recordSize() {
        val drawable = drawable
        if (recordWidth == 0 && recordHeight == 0 && drawable != null) {
            val screen = SysContext.getScreenWidth()
            recordWidth = screen
            recordHeight = (screen * 1f / drawable.intrinsicWidth * drawable.intrinsicHeight).toInt()
        }
    }

    class Target(view: OnceImageView) : CustomViewTarget<OnceImageView, Drawable>(view) {
        override fun onLoadFailed(errorDrawable: Drawable?) {
            view.setImageBitmap(null)
            view.setImageDrawable(errorDrawable)
            view.recordSize()

            view.isLoading = false
            view.isSuccess = false
        }

        override fun onResourceCleared(placeholder: Drawable?) {
            view.setImageBitmap(null)
            view.setImageDrawable(placeholder)
        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            view.setImageBitmap(null)
            view.setImageDrawable(resource)
            view.recordSize()

            view.isLoading = false
            view.isSuccess = true
        }

    }
}
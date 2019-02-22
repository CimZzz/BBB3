package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gogoh5.apps.quanmaomao.library.R
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz

class AutoClearImageView: ImageView {
    private var src: Int = -1

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArr = context?.obtainStyledAttributes(attrs, R.styleable.AutoClearImageView)
        if(typedArr != null) {
            src = typedArr.getResourceId(R.styleable.AutoClearImageView_src, -1)
        }
        typedArr?.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        logCimZzz("onAttachedToWindow")
        if(src != -1)
            SysContext.getGlide().loadResourceDirectly(src, this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        logCimZzz("onDetachedFromWindow")
        if(src != -1)
            SysContext.getGlide().cancel(this)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)


        if(src != -1 && (
                    changedView == this ||
                            changedView == this.parent
                    )) {
            logCimZzz("Visible Changed $visibility")
            if(visibility == View.VISIBLE)
                SysContext.getGlide().loadResourceDirectly(src, this)
            else SysContext.getGlide().cancel(this)
        }
    }

}
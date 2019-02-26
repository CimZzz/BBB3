package com.gogoh5.apps.quanmaomao.android.ui.test

import android.app.ActionBar
import android.graphics.Color
import android.support.v4.app.ActivityOptionsCompat
import android.text.Layout
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BasePresenter
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.widgets.TouchableImageView

class TestUI: BaseUI<BasePresenter<*>>() {
    lateinit var view: TouchableImageView
    override fun initPresenter(): BasePresenter<*> = BasePresenter.EMPTY

    override fun initView() {

        view = TouchableImageView(this)
        view.setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.setImageResource(R.drawable.img_ali_auth)
        val parent = FrameLayout(this)
        parent.setBackgroundColor(Color.BLACK)
        parent.setSize(SysContext.getScreenWidth(), SysContext.getScreenHeight())
        parent.addView(view)
        view.setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(parent)

        ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "img")
    }
}
package com.gogoh5.apps.quanmaomao.android.entities.controllers

import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HomeHeadRenderer
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController
import com.gogoh5.apps.quanmaomao.library.extensions.setAppBarLayoutFlag
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.item_home_head.view.*
import java.util.HashMap

class HomeController(parent: ViewGroup) : ListPageBaseHeaderController<HomeHeadRenderer>(parent) {
    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.item_home_head)

    override fun initView(parent: ViewGroup, shortViewCacheMap: HashMap<String, List<View>>?) {
        itemView.setAppBarLayoutFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL)
        itemView.searchBarBg.tapWith {
            callback.onEvent(dataType)
        }
    }
}
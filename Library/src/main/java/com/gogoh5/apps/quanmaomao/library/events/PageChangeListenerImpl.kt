package com.gogoh5.apps.quanmaomao.library.events

import android.support.v4.view.ViewPager
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrollStateChanged
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrolled
import com.gogoh5.apps.quanmaomao.library.extensions.PageSelected

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/15 21:29:50
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class PageChangeListenerImpl(
    val name: String?,
    val actionSource: ActionSource,
    val selected: PageSelected?,
    val stateChanged: PageScrollStateChanged?,
    val scrolled: PageScrolled?
) : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
        stateChanged?.let { it(state) }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        scrolled?.let { it(position, positionOffset, positionOffsetPixels) }
    }

    override fun onPageSelected(position: Int) {
        selected?.let { it(position) }
    }

}
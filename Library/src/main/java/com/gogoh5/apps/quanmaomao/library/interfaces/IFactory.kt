package com.gogoh5.apps.quanmaomao.library.interfaces

import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrollStateChanged
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrolled
import com.gogoh5.apps.quanmaomao.library.extensions.PageSelected
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRun

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/15 13:33:11
 *  Project : taoke_android
 *  Since Version : Alpha
 */
interface IFactory {
    fun genClickListener(name: String?, actionSource: ActionSource, action: TakeRun<View?>) : View.OnClickListener

    fun genTextWatcher(name: String?, actionSource: ActionSource, action: TakeRun<Editable?>) : TextWatcher

    fun genPageChangeListener(name: String?,
                              actionSource: ActionSource,
                              selected: PageSelected?,
                              stateChanged: PageScrollStateChanged?,
                              scrolled: PageScrolled?) : ViewPager.OnPageChangeListener
}
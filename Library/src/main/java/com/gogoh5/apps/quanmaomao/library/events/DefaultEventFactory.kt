package com.gogoh5.apps.quanmaomao.library.events

import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrollStateChanged
import com.gogoh5.apps.quanmaomao.library.extensions.PageScrolled
import com.gogoh5.apps.quanmaomao.library.extensions.PageSelected
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRun
import com.gogoh5.apps.quanmaomao.library.interfaces.IFactory

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/15 12:24:09
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object DefaultEventFactory : IFactory {
    override fun genTextWatcher(name: String?, actionSource: ActionSource, action: TakeRun<Editable?>): TextWatcher =
        TextWatcherImpl(name, actionSource, action)

    override fun genClickListener(name: String?, actionSource: ActionSource, action: TakeRun<View?>) : View.OnClickListener =
        ClickListenerImpl(name, actionSource, action)

    override fun genPageChangeListener(
        name: String?,
        actionSource: ActionSource,
        selected: PageSelected?,
        stateChanged: PageScrollStateChanged?,
        scrolled: PageScrolled?
    ): ViewPager.OnPageChangeListener =
        PageChangeListenerImpl(
            name,
            actionSource,
            selected,
            stateChanged,
            scrolled
        )
}
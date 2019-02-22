package com.gogoh5.apps.quanmaomao.library.events

import android.text.Editable
import android.text.TextWatcher
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRun

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/15 13:19:42
 *  Project : taoke_android
 *  Since Version : Alpha
 */
open class TextWatcherImpl(val name: String? = null, val actionSource: ActionSource, val action: TakeRun<Editable?>) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        action(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
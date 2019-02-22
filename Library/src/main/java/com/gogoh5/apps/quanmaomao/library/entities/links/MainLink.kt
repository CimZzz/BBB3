package com.gogoh5.apps.quanmaomao.library.entities.links

import android.content.Context
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink

class MainLink(
    val transferPage: Int? = null,
    val isClose: Boolean? = null
): BaseLink() {
    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName("com.gogoh5.apps.quanmaomao.android.ui.main.MainUI"))
        processIntent(intent, context)
        context.startActivity(intent)
    }
}
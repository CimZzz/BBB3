package com.gogoh5.apps.quanmaomao.library.entities.links

import android.content.Context
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink

class WebLink(
    val url: String,
    val useAliTrade: Boolean = false,
    val aliPage: Int? = null,
    val isAliAuth: Boolean = false
): BaseLink() {
    companion object {
        const val PAGE_ORDER = 0
        const val PAGE_CART = 1
    }

    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName("com.gogoh5.apps.quanmaomao.android.ui.web.WebUI"))
        processIntent(intent, context)
        context.startActivity(intent)
    }
}
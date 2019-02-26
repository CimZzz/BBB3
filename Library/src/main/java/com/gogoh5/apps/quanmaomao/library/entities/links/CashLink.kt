package com.gogoh5.apps.quanmaomao.library.entities.links

import android.content.Context
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink

class CashLink: BaseLink() {
    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName("com.gogoh5.apps.quanmaomao.android.ui.cash.CashUI"))
        processIntent(intent, context)
        context.startActivity(intent)
    }
}
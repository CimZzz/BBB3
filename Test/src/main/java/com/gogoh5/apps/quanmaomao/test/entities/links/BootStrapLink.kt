package com.gogoh5.apps.quanmaomao.test.entities.links

import android.content.Context
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink

class BootStrapLink: BaseLink() {

    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName("com.gogoh5.apps.quanmaomao.test.uis.bootstraip.BootstrapUI"))
        processIntent(intent, context)
        context.startActivity(intent)
    }
}
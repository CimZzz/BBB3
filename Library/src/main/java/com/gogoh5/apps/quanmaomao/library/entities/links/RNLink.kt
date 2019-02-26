package com.gogoh5.apps.quanmaomao.library.entities.links

import android.content.Context
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink

class RNLink(
    val index: String,
    val componentName: String,
    val directlyName: String? = null
): BaseLink() {
    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName(directlyName?:"com.gogoh5.apps.quanmaomao.library.base.BaseRNUI"))
        processIntent(intent, context)
        context.startActivity(intent)
    }
}
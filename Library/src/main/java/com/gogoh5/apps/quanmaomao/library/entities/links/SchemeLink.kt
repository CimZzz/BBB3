package com.gogoh5.apps.quanmaomao.library.entities.links

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.utils.ToastUtils
import java.lang.Exception

class SchemeLink(
    val scheme: String,
    val errorTxt: String? = null
): BaseLink() {
    override fun doPerform(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scheme))
            if(intent.resolveActivity(context.packageManager) != null) {
                processIntent(intent, context)
                context.startActivity(intent)
                return
            }

        }
        catch (e: Exception) {

        }
        if(errorTxt != null)
            ToastUtils.sendToast(errorTxt)
    }
}
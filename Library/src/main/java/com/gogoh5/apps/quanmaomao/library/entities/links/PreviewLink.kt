package com.gogoh5.apps.quanmaomao.library.entities.links

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.gogoh5.apps.quanmaomao.library.base.BaseLink


class PreviewLink(
    val url: String,
    @Transient val transitionView: View? = null
): BaseLink() {
    override fun doPerform(context: Context) {
        val intent = Intent(context, Class.forName( "com.gogoh5.apps.quanmaomao.android.ui.preview.PreviewUI"))
        processIntent(intent, context)
        if(context is Activity && transitionView != null)
            ContextCompat.startActivity(context, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(context, transitionView, "previewImg").toBundle())
        else context.startActivity(intent)
    }
}
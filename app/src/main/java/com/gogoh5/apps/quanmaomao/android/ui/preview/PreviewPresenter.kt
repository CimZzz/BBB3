package com.gogoh5.apps.quanmaomao.android.ui.preview

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.links.PreviewLink

class PreviewPresenter(view: IPreviewView) : BaseMethodPresenter<IPreviewView, PreviewMethod>(view) {

    override fun generateMethod(): PreviewMethod = PreviewMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        val link = view.getPassLink() as PreviewLink

        view.showImage(link.url)
    }
}
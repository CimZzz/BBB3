package com.gogoh5.apps.quanmaomao.android.ui.preview

import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.ui_preview.*

class PreviewUI: BaseUI<PreviewPresenter>(), IPreviewView {

    override fun initPresenter(): PreviewPresenter = PreviewPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_preview)

        previewImg.tapWith {
            closeDirectly()
        }
    }

    override fun showImage(url: String) {
        SysContext.getGlide().loadNetPicDirectly(url, previewImg)
    }

    override fun closeDirectly() {
        supportFinishAfterTransition()
    }
}
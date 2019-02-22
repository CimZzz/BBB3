package com.gogoh5.apps.quanmaomao.android.ui.main

import android.app.Activity
import android.content.Intent
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.links.MainLink
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class MainPresenter(view: IMainView) : BaseMethodPresenter<IMainView, MainMethod>(view) {
    var isOpenAliAuth: Boolean = false

    override fun generateMethod(): MainMethod = MainMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {

    }

    override fun newIntent(intent: Intent?) {
        val mixDataBundle = MixDataBundle(passIntent = intent)
        val link: BaseLink = mixDataBundle.getObject(BaseUI.MASK_LINK)?:return

        if(link is MainLink) {
            if(link.isClose == true) {
                view.closeDirectly()
                return
            }

            if(link.transferPage != null) {
                view.transferToPage(link.transferPage!!)
                return
            }
        }
    }

    override fun onViewEnter() {
        super.onViewEnter()
        if(!method.checkAliAuth()) {
            if(!isOpenAliAuth) {
                isOpenAliAuth = true
                LinkUtils.linkAliAuthDialog(view.getContext())
            }
            else view.closeDirectly()
        }
    }

    fun getActivity() = view.getContext() as Activity

    fun backward() {
        view.backward()
    }

//    fun transferCartPage() {
//        view.transferToPage(2)
//    }

    fun transferToSearchPage() {
        view.transferToPage(1)
    }

    fun refreshAll() {
        view.refreshAll()
    }
}
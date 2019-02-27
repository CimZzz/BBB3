package com.gogoh5.apps.quanmaomao.android.ui.main.me

import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean
import com.gogoh5.apps.quanmaomao.library.entities.links.WebLink
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils

class MePagePresenter(view: IMePageView) : BaseMethodPresenter<IMePageView, MePageMethod>(view) {
    override fun generateMethod(): MePageMethod = MePageMethod(this)

    override fun onInitPresenter(mixDataBundle: MixDataBundle) {
        refreshMeBeanWithLoading()
        method.initData()
    }

    override fun onViewEnter() {
        refreshMeBean()
    }

    override fun onViewQuit() {
        view.hideRefreshing()
    }

    internal fun refreshMeBeanWithLoading() {
        view.showLoading()
        refreshMeBean()
    }


    internal fun refreshMeBean() {
        method.refreshMeBean()
    }

    override fun onLazyLoad() {
        val meLazyLoadBean = getLazyLoadParams(MePage.LAZY_LOAD_MONEY_INFO)

        if(meLazyLoadBean != null) {
            if(meLazyLoadBean.isSuccess()) {
                val meBean = meLazyLoadBean.obj as MeBean
                if(!markDone)
                    markDoneAction()
                view.updateContent(meBean)
                view.hideRefreshing()
            }
            else if(meLazyLoadBean.isFailure() && !markDone)
                view.showError()
            consumeLazyLoadParams(MePage.LAZY_LOAD_MONEY_INFO)
        }

        if(markDone) {
            val nickName = getLazyLoadParams(MePage.LAZY_LOAD_NICK_NAME)
            if (nickName != null) {
                view.updateNickName(nickName.obj as String?)
                consumeLazyLoadParams(MePage.LAZY_LOAD_NICK_NAME)
            }

            val avatar = getLazyLoadParams(MePage.LAZY_LOAD_AVATAR)
            if (avatar != null) {
                view.updateAvatar(avatar.obj as String?)
                consumeLazyLoadParams(MePage.LAZY_LOAD_AVATAR)
            }

            val shortId = getLazyLoadParams(MePage.LAZY_LOAD_SHORT_ID)
            if (shortId != null) {
                view.updateShortId(shortId.obj as String?)
                consumeLazyLoadParams(MePage.LAZY_LOAD_SHORT_ID)
            }
        }

    }

    fun linkSetting() {
        LinkUtils.linkSetting(view.getContext())
    }

    fun linkReward() {
        LinkUtils.linkRN("index", "HelloWorld", context = view.getContext())
    }

    fun linkOrder() {
        LinkUtils.linkWeb("", aliPage = WebLink.PAGE_ORDER, context = view.getContext())
    }

    fun linkCashList() {
        LinkUtils.linkCashList(view.getContext())
    }
}
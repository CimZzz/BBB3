package com.gogoh5.apps.quanmaomao.android.ui.main.me

import com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen.MeLazyLoadBean
import com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen.StringLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseMethodPresenter
import com.gogoh5.apps.quanmaomao.library.base.MixDataBundle
import com.gogoh5.apps.quanmaomao.library.entities.links.WebLink
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz
import com.gogoh5.apps.quanmaomao.library.utils.logRequest

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
        val meLazyLoadBean: MeLazyLoadBean? = getLazyLoadParams(MePage.LAZY_LOAD_MONEY_INFO)

        if(meLazyLoadBean != null) {
            if(meLazyLoadBean.isSuccess()) {
                if(!markDone)
                    markDoneAction()
                view.updateContent(meLazyLoadBean.meBean!!)
                view.hideRefreshing()
            }
            else if(meLazyLoadBean.isFailure() && !markDone)
                view.showError()
            consumeLazyLoadParams(MePage.LAZY_LOAD_MONEY_INFO)
        }

        if(markDone) {
            val nickName: StringLazyLoadBean? = getLazyLoadParams(MePage.LAZY_LOAD_NICK_NAME)
            if (nickName != null) {
                view.updateNickName(nickName.value)
                consumeLazyLoadParams(MePage.LAZY_LOAD_NICK_NAME)
            }

            val avatar: StringLazyLoadBean? = getLazyLoadParams(MePage.LAZY_LOAD_AVATAR)
            if (avatar != null) {
                view.updateAvatar(avatar.value)
                consumeLazyLoadParams(MePage.LAZY_LOAD_AVATAR)
            }

            val shortId: StringLazyLoadBean? = getLazyLoadParams(MePage.LAZY_LOAD_SHORT_ID)
            if (shortId != null) {
                view.updateShortId(shortId.value)
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
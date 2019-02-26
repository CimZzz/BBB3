package com.gogoh5.apps.quanmaomao.android.ui.main.me

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.ui.main.BaseMainPage
import com.gogoh5.apps.quanmaomao.android.ui.main.MainPresenter
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler
import kotlinx.android.synthetic.main.page_me.view.*
import kotlinx.android.synthetic.main.sub_me_content.view.*

class MePage(mainPresenter: MainPresenter) : BaseMainPage<Unit>(mainPresenter), IMePageView {
    companion object {
        const val LAZY_LOAD_MONEY_INFO = 0
        const val LAZY_LOAD_NICK_NAME = 2
        const val LAZY_LOAD_SHORT_ID = 3
        const val LAZY_LOAD_AVATAR = 4
    }

    val presenter = MePagePresenter(this)

    override fun generateDataParcelable() {}

    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.page_me)

    override fun isViewFromObject(view: View): Boolean = rootView == view

    override fun initViewPage(parent: ViewGroup, bindBean: Any, position: Int) {
        super.initViewPage(parent, bindBean, position)
        presenter.initPresenter(null, null)

        rootView.topGuide.setSize(height = SysContext.getStatusBarHeight())

        (rootView as ViewHandler).viewStubFirstBind = {
            view, id ->
            when(id) {
                R.id.error -> {
                    view.tapWith("我的页面错误刷新", ActionSource.MAIN_ME_ERROR) {
                        presenter.refreshMeBeanWithLoading()
                    }
                }
                R.id.content -> {
                    view.refreshView.setOnRefreshListener {
                        presenter.refreshMeBean()
                    }

                    view.settingBtn.tapWith("我的页面设置按钮", ActionSource.MAIN_ME_SETTING) {
                        presenter.linkSetting()
                    }

                    view.balanceContainer.tapWith("我的页面余额", ActionSource.MAIN_ME_BALANCE) {
                        presenter.linkCashList()
                    }

                    view.rewardContainer.tapWith("我的页面返利", ActionSource.MAIN_ME_REWARD) {
                        presenter.linkReward()
                    }

                    view.orderContainer.tapWith("我的页面订单", ActionSource.MAIN_ME_ORDER) {
                        presenter.linkOrder()
                    }

//                    view.cartContainer.tapWith("我的页面购物车", ActionSource.MAIN_ME_CART) {
//                        mainPresenter.transferCartPage()
//                    }

                    view.aliAuthContainer.tapWith("我的页面淘宝授权", ActionSource.MAIN_ME_ALI_AUTH) {
                        presenter.linkSetting()
                    }
                }
            }
        }
    }

    override fun attachView(parent: ViewGroup) {
        parent.addView(rootView)
    }

    override fun detachView(parent: ViewGroup) {
        parent.removeView(rootView)
    }

    override fun onEnterPage() {
        presenter.viewEnter(isFirstEnter)
    }

    override fun onQuitPage() {
        presenter.viewQuit()
    }

    override fun onDestroyPage() {
        presenter.viewDestroy()
    }

    override fun getContext(): Context = mainPresenter.getActivity()

    override fun showLoading() {
        (rootView as ViewHandler).showView(R.id.loading)
    }

    override fun showError() {
        (rootView as ViewHandler).showView(R.id.error)
    }

    override fun hideRefreshing() {
        val contentView = (rootView as ViewHandler).showView(R.id.content)?:return
        contentView.refreshView.isRefreshing = false
    }

    override fun updateContent(meBean: MeBean) {
        val contentView = (rootView as ViewHandler).showView(R.id.content)?:return

        contentView.totalSettleTxt.text = StringUtils.formatPrice(meBean.totalSettle)
        contentView.profitTxt.text = StringUtils.formatPrice(meBean.estimate)
        contentView.balanceTxt.text = StringUtils.formatPrice(meBean.balance)
    }

    override fun updateNickName(nickName: String?) {
        val contentView = (rootView as ViewHandler).showView(R.id.content)?:return

        contentView.nickNameTxt.text = nickName?:"未设置昵称"
    }

    override fun updateShortId(shortId: String?) {
        val contentView = (rootView as ViewHandler).showView(R.id.content)?:return

        contentView.idTxt.text = "ID:${shortId?:"-----"}"
    }

    override fun updateAvatar(avatar: String?) {
        val contentView = (rootView as ViewHandler).showView(R.id.content)?:return

        SysContext.getGlide().loadNetPicDirectly(avatar, R.drawable.icon_user_default_1, contentView.avatarImg)
    }
}
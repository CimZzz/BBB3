package com.gogoh5.apps.quanmaomao.android.ui.main.home

import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.ui.main.BaseMainPage
import com.gogoh5.apps.quanmaomao.android.ui.main.MainPresenter
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPage
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.page_home.view.*

class HomePage(mainPresenter: MainPresenter) : BaseMainPage<Unit>(mainPresenter), HomePageContext.Callback {
    val pageContext = HomePageContext(mainPresenter.getActivity(), this)
    val innerPage = ListPage(pageContext)


    override fun generateDataParcelable() = Unit

    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.page_home)

    override fun initViewPage(parent: ViewGroup, bindBean: Any, position: Int) {
        super.initViewPage(parent, bindBean, position)
        rootView.topGuide.setSize(height = SysContext.getStatusBarHeight())
        innerPage.initViewPage(rootView.pageContainer, innerPage.generateDataParcelable(), 0)
    }

    override fun isViewFromObject(view: View): Boolean = view == rootView

    override fun attachView(parent: ViewGroup) {
        parent.addView(rootView)
        innerPage.attachView(rootView.pageContainer)
    }

    override fun detachView(parent: ViewGroup) {
        parent.removeView(rootView)
        innerPage.detachView(rootView.pageContainer)
    }

    override fun onEnterPage() {
        innerPage.enterPage()
    }

    override fun onQuitPage() {
        innerPage.quitPage()
    }

    override fun onDestroyPage() {
        innerPage.destroyPage()
    }

    override fun transferToSearchPage() {
        mainPresenter.transferToSearchPage()
    }
}
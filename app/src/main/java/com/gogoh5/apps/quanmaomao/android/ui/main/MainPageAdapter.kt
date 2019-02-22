package com.gogoh5.apps.quanmaomao.android.ui.main

import com.gogoh5.apps.quanmaomao.android.ui.main.cart.CartPage
import com.gogoh5.apps.quanmaomao.android.ui.main.home.HomePage
import com.gogoh5.apps.quanmaomao.android.ui.main.me.MePage
import com.gogoh5.apps.quanmaomao.android.ui.main.search.SearchPage
import com.gogoh5.apps.quanmaomao.library.base.BasePage
import com.gogoh5.apps.quanmaomao.library.base.BasePageAdapter

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/15 18:28:34
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class MainPageAdapter(private val mainPresenter: MainPresenter) : BasePageAdapter() {
    override fun generateBasePage(position: Int): BasePage<*> = when (position) {
            0-> HomePage(mainPresenter)
            1-> SearchPage(mainPresenter)
//            2-> CartPage(mainPresenter)
            2-> MePage(mainPresenter)
            else-> HomePage(mainPresenter)
//            1->TestPager(Color.CYAN)
//            1-> SuperSearchPage(mainPresenter)
//            else->MePage(mainPresenter)
//            else-> ListPage(ProductListPageContext())
        }

    override fun getCount(): Int = 3
}
package com.gogoh5.apps.quanmaomao.android.ui.main

import android.os.SystemClock
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BasePageAdapter
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.pageChange
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz
import kotlinx.android.synthetic.main.ui_main.*

class MainUI: BaseUI<MainPresenter>(), IMainView {
    private var lastPage: Int = 0
    private var lastPage2: Int = 0
    private var lastBackTime: Long = 0L

    override fun initPresenter(): MainPresenter = MainPresenter(this)

    override fun initView() {
        SysContext.makeNavigationTransparent(this)
        setContentView(R.layout.ui_main)
        bottomHomeContainer.tapWith("首页主页按钮", ActionSource.MAIN_HOME) {
            if(bottomHomeContainer.isSelected) {
                val lastClickTime: Long = bottomHomeContainer.tag as Long? ?:0L
                val currentClickTime = SystemClock.uptimeMillis()
                if(currentClickTime - lastClickTime < 200) {
                    bottomHomeContainer.tag = 0L
                }
                else bottomHomeContainer.tag = currentClickTime
                return@tapWith
            }

            viewPager.setCurrentItem(0, false)
        }

        bottomSearchContainer.tapWith("首页搜索按钮", ActionSource.MAIN_SEARCH) {
            if(bottomSearchContainer.isSelected)
                return@tapWith

            viewPager.setCurrentItem(1, false)
        }

//        bottomCartContainer.tapWith("首页购物车按钮", ActionSource.MAIN_CART) {
//            if(bottomCartContainer.isSelected)
//                return@tapWith
//
//            viewPager.setCurrentItem(2, false)
//        }

        bottomMeContainer.tapWith("首页我的按钮", ActionSource.MAIN_ME) {
            if(bottomMeContainer.isSelected)
                return@tapWith

            viewPager.setCurrentItem(2, false)
        }

        viewPager.adapter = MainPageAdapter(presenter)
        viewPager.pageChange ("首页ViewPager", selected = {
            lastPage = lastPage2
            lastPage2 = it
            when(it) {
                0->checkBottomBar(bottomHomeContainer.id)
                1->checkBottomBar(bottomSearchContainer.id)
//                2->checkBottomBar(bottomCartContainer.id)
                2->checkBottomBar(bottomMeContainer.id)
            }
        })

        checkBottomBar(bottomHomeContainer.id)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val currentTime = SystemClock.elapsedRealtime()
        if(currentTime - lastBackTime < 800L) {
            presenter.quitAll()
            return
        }
        else presenter.sendToast("再按一次退出券猫猫")
        lastBackTime = currentTime
    }


    override fun closeDirectly() {
        super.finish()
    }

    override fun finish() {
//        logCimZzz("MainUI finish")
//        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        val adapter = viewPager.adapter
        if(adapter != null)
            (adapter as BasePageAdapter).destroy()
    }



    private fun checkBottomBar(id: Int) {
        if(bottomHomeContainer.id == id) {
            if(!bottomHomeContainer.isSelected) {
                bottomHomeContainer.isSelected = true
                bottomHomeImg.isSelected = true
                bottomHomeTxt.isSelected = true
            }
        }
        else if(bottomHomeContainer.isSelected) {
            bottomHomeContainer.isSelected = false
            bottomHomeImg.isSelected = false
            bottomHomeTxt.isSelected = false
        }


        if(bottomSearchContainer.id == id) {
            if(!bottomSearchContainer.isSelected) {
                bottomSearchContainer.isSelected = true
                bottomSearchImg.isSelected = true
                bottomSearchTxt.isSelected = true
            }
        }
        else if(bottomSearchContainer.isSelected) {
            bottomSearchContainer.isSelected = false
            bottomSearchImg.isSelected = false
            bottomSearchTxt.isSelected = false
        }


        if(bottomCartContainer.id == id) {
            if(!bottomCartContainer.isSelected) {
                bottomCartContainer.isSelected = true
                bottomCartImg.isSelected = true
                bottomCartTxt.isSelected = true
            }
        }
        else if(bottomCartContainer.isSelected) {
            bottomCartContainer.isSelected = false
            bottomCartImg.isSelected = false
            bottomCartTxt.isSelected = false
        }


        if(bottomMeContainer.id == id) {
            if(!bottomMeContainer.isSelected) {
                bottomMeContainer.isSelected = true
                bottomMeImg.isSelected = true
                bottomMeTxt.isSelected = true
            }
        }
        else if(bottomMeContainer.isSelected) {
            bottomMeContainer.isSelected = false
            bottomMeImg.isSelected = false
            bottomMeTxt.isSelected = false
        }
    }


    override fun backward() {
        viewPager.setCurrentItem(lastPage, true)
    }

    override fun transferToPage(page: Int) {
        viewPager.setCurrentItem(page, true)
    }

    override fun refreshAll() {
        viewPager.adapter = null
        val adapter = viewPager.adapter
        if(adapter != null)
            (adapter as BasePageAdapter).destroy()

        viewPager.adapter = MainPageAdapter(presenter)


        viewPager.setCurrentItem(lastPage2, false)
    }

}
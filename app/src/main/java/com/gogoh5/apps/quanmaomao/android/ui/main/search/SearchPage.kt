package com.gogoh5.apps.quanmaomao.android.ui.main.search

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.ui.main.BaseMainPage
import com.gogoh5.apps.quanmaomao.android.ui.main.MainPresenter
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.extensions.selectionEnd
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.page_search.view.*

class SearchPage(mainPresenter: MainPresenter) : BaseMainPage<Unit>(mainPresenter), ISearchPageView {
    val presenter = SearchPagePresenter(this)

    override fun generateDataParcelable() {
    }

    override fun isViewFromObject(view: View): Boolean = rootView == view

    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.page_search)

    override fun initViewPage(parent: ViewGroup, bindBean: Any, position: Int) {
        super.initViewPage(parent, bindBean, position)
        presenter.initPresenter(null, null)

        rootView.topGuide.setSize(height = SysContext.getStatusBarHeight())

        rootView.backBtn.tapWith("搜索页返回按钮", ActionSource.MAIN_SEARCH_BACK) {
            mainPresenter.backward()
        }

        rootView.searchBtn.tapWith("搜索页搜索按钮", ActionSource.MAIN_SEARCH_SEARCH) {
            presenter.openSearch(rootView.searchContentTxt.text.toString())
        }

        rootView.delBtn.tapWith("搜索页清空搜索历史", ActionSource.MAIN_SEARCH_DEL) {
            presenter.clearHistory()
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

    override fun configHistory(historyArr: List<String>?) {
        if(!historyArr.isNullOrEmpty()) {
            rootView.historyContent.removeAllViews()
            rootView.searchHistoryContainer.visibility = View.VISIBLE

            historyArr.forEach {
                val textView: TextView = ViewUtils.inflateView(rootView.historyContent, R.layout.item_search_item)
                textView.text = it
                textView.tapWith("历史搜索项", ActionSource.MAIN_SEARCH_HISTORY_ITEM) {
                    presenter.openSearch(textView.text.toString())
                }
                rootView.historyContent.addView(textView)
            }
        }
        else {
            rootView.historyContent.removeAllViews()
            rootView.searchHistoryContainer.visibility = View.GONE
        }
    }

    override fun configHotSearch(hotSearchArr: List<String>?) {
        if(!hotSearchArr.isNullOrEmpty()) {
            rootView.hotSearchContent.removeAllViews()
            rootView.hotSearchContainer.visibility = View.VISIBLE

            hotSearchArr.forEach {
                val textView: TextView = ViewUtils.inflateView(rootView.hotSearchContent, R.layout.item_search_item)
                textView.text = it
                textView.tapWith("热门搜索项", ActionSource.MAIN_SEARCH_HISTORY_ITEM) {
                    presenter.openSearch(textView.text.toString())
                }
                rootView.hotSearchContent.addView(textView)
            }
        }
        else {
            rootView.hotSearchContent.removeAllViews()
            rootView.hotSearchContainer.visibility = View.GONE
        }
    }

    override fun fillSearchBar(content: String) {
        rootView.searchContentTxt.setText(content)
        rootView.searchContentTxt.selectionEnd()
    }

    override fun getContext(): Context = mainPresenter.getActivity()
}
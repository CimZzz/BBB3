package com.gogoh5.apps.quanmaomao.android.entities.controllers

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.entities.renderers.HotSearchRenderer
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController
import com.gogoh5.apps.quanmaomao.library.extensions.forEach
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.item_hot_search.view.*
import java.util.*

class HotSearchController(parent: ViewGroup) : ListPageBaseHeaderController<HotSearchRenderer>(parent) {
    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.item_hot_search)

    override fun initView(parent: ViewGroup, shortViewCacheMap: HashMap<String, List<View>>?) {
        val hotwordArr = SysContext.getSetting().initBean.hotword
        if(!hotwordArr.isNullOrEmpty()) {
            itemView.visibility = View.VISIBLE
            val cacheViewList: MutableList<View>? = shortViewCacheMap?.get("HotSearch") as MutableList<View>?
            hotwordArr.forEach {
                val textView: TextView
                if(!cacheViewList.isNullOrEmpty()) {
                    textView = cacheViewList[0] as TextView
                    cacheViewList.removeAt(0)
                }
                else textView = ViewUtils.inflateView(itemView.hotSearchContentContainer, R.layout.item_search_item)

                textView.text = it
                textView.tapWith {
                    callback.onEvent(dataType, textView.text.toString())
                }

                itemView.hotSearchContentContainer.addView(textView)
            }
        }
        else itemView.visibility = View.GONE
    }

    override fun onCacheView(viewCacheMap: HashMap<String, List<View>>) {
        super.onCacheView(viewCacheMap)
        if(itemView.hotSearchContentContainer.childCount != 0) {
            val list = LinkedList<View>()
            itemView.hotSearchContentContainer.forEach {
                list.add(it)
            }
            itemView.hotSearchContentContainer.removeAllViews()
            viewCacheMap.put("HotSearch", list)
        }
    }
}
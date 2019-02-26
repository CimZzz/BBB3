package com.gogoh5.apps.quanmaomao.android.entities.filterbars

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController
import com.gogoh5.apps.quanmaomao.library.extensions.forEach
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.filterbar_product.view.*
import java.util.*

class ProductFilterBar(parent: ViewGroup, val hasHotSearch: Boolean = true) : ListPageBaseHeaderController<BaseRenderer>(parent) {
    companion object {
        const val KEY_ORDER = "__Key_Order"
    }

    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.filterbar_product)

    override fun initView(parent: ViewGroup, shortViewCacheMap: HashMap<String, List<View>>?) {
        val hotwordArr = SysContext.getSetting().initBean.hotword

        if(hasHotSearch && !hotwordArr.isNullOrEmpty()) {
            itemView.hotSearchContainer.visibility = View.VISIBLE

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
        else itemView.hotSearchContainer.visibility = View.GONE


        itemView.normalBg.tapWith {
            callback.setVariableValue(KEY_ORDER, 0)
            checkUpdate()

            callback.refreshContent()
        }

        itemView.saleCountBg.tapWith {
            callback.setVariableValue(KEY_ORDER, 4)
            checkUpdate()
            callback.refreshContent()
        }

        itemView.discountBg.tapWith {
            callback.setVariableValue(KEY_ORDER, 1)
            checkUpdate()
            callback.refreshContent()
        }

        itemView.priceContainer.tapWith {
            val currentOrder = callback.getVariableValue(KEY_ORDER, 0)
            if(currentOrder == 2)
                callback.setVariableValue(KEY_ORDER, 3)
            else callback.setVariableValue(KEY_ORDER, 2)
            checkUpdate()

            callback.refreshContent()
        }
    }

    override fun onVisible(parent: ViewGroup) {
        super.onVisible(parent)
        checkUpdate()
    }

    private fun checkUpdate() {
        val currentKeyOrder = callback.getVariableValue(KEY_ORDER, 0)

        if(currentKeyOrder == 0) {
            itemView.normalTxt.isSelected = true
            itemView.normalLine.visibility = View.VISIBLE
        }
        else {
            itemView.normalTxt.isSelected = false
            itemView.normalLine.visibility = View.GONE
        }

        if(currentKeyOrder == 4) {
            itemView.saleCountTxt.isSelected = true
            itemView.saleCountLine.visibility = View.VISIBLE
        }
        else {
            itemView.saleCountTxt.isSelected = false
            itemView.saleCountLine.visibility = View.GONE
        }

        if(currentKeyOrder == 1) {
            itemView.discountTxt.isSelected = true
            itemView.discountLine.visibility = View.VISIBLE
        }
        else {
            itemView.discountTxt.isSelected = false
            itemView.discountLine.visibility = View.GONE
        }

        if(currentKeyOrder == 2) {
            itemView.priceTxt.isSelected = true
            itemView.priceIconImg.setBackgroundResource(R.drawable.icon_filter_up)
            itemView.priceLine.visibility = View.VISIBLE
        }
        else if(currentKeyOrder == 3) {
            itemView.priceTxt.isSelected = true
            itemView.priceIconImg.setBackgroundResource(R.drawable.icon_filter_down)
            itemView.priceLine.visibility = View.VISIBLE
        }
        else {
            itemView.priceTxt.isSelected = false
            itemView.priceIconImg.setBackgroundResource(R.drawable.icon_filter_default)
            itemView.priceLine.visibility = View.GONE
        }
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
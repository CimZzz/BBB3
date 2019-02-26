package com.gogoh5.apps.quanmaomao.android.entities.holders

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.entities.databeans.CashRenderer
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseContentHolder
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.item_cash.view.*

class CashHolder(parent: ViewGroup) : ListPageBaseContentHolder<CashRenderer>(ViewUtils.inflateView(parent, R.layout.item_cash)) {
    override fun bindBean() {
        itemView.titleTxt.text = bean.title
        itemView.priceTxt.text = StringUtils.formatPrice(bean.applyNum)
        itemView.timeTxt.text = bean.createTime
        itemView.statusTxt.text = bean.status
        itemView.statusTxt.setTextColor(bean.color)
    }
}
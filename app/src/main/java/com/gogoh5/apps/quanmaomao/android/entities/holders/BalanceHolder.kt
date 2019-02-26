package com.gogoh5.apps.quanmaomao.android.entities.holders

import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.entities.databeans.BalanceRenderer
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseContentHolder
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.item_balance.view.*

class BalanceHolder(parent: ViewGroup) : ListPageBaseContentHolder<BalanceRenderer>(ViewUtils.inflateView(parent, R.layout.item_balance)) {

    override fun bindBean() {
        itemView.titleTxt.text = bean.balanceType
        itemView.priceTxt.text = StringUtils.formatPrice(bean.changeNum)
        itemView.timeTxt.text = bean.createTime
    }
}
package com.gogoh5.apps.quanmaomao.android.entities.controllers

import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.android.entities.renderers.CashHeadRenderer
import com.gogoh5.apps.quanmaomao.library.entities.links.BalanceDetailLink
import com.gogoh5.apps.quanmaomao.library.entities.links.CashLink
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extended.listview.ListPageBaseHeaderController
import com.gogoh5.apps.quanmaomao.library.extensions.setAppBarLayoutFlag
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import kotlinx.android.synthetic.main.item_cash_head.view.*
import java.util.HashMap


class CashController(parent: ViewGroup) : ListPageBaseHeaderController<CashHeadRenderer>(parent) {
    override fun generateRootView(parent: ViewGroup): View = ViewUtils.inflateView(parent, R.layout.item_cash_head)

    override fun initView(parent: ViewGroup, shortViewCacheMap: HashMap<String, List<View>>?) {
        itemView.setAppBarLayoutFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL)
        itemView.balanceDetailBtn.tapWith {
            callback.onClick(dataType, BalanceDetailLink())
        }

        itemView.cashBtn.tapWith {
            callback.onClick(dataType, CashLink())
        }
    }

    override fun onVisible(parent: ViewGroup) {
        super.onVisible(parent)
        val meBean = SysContext.getUser().getMeBean(false)?:return
        itemView.priceTxt.text = StringUtils.formatPrice(meBean.balance)
    }
}
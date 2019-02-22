package com.gogoh5.apps.quanmaomao.android.ui.cash

import com.gogoh5.apps.quanmaomao.library.base.IBaseUIView

interface ICashView: IBaseUIView {
    fun configBalance(balance: Double)
    fun configPrice(price: Double)


    fun getPriceStr(): String
    fun getAliAccountStr(): String
    fun getNameStr(): String
    fun getMobileStr(): String
}
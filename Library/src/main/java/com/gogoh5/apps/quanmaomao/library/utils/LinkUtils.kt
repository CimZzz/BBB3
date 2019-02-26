package com.gogoh5.apps.quanmaomao.library.utils

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.entities.links.*
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/2/15 14:55:04
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object LinkUtils {
    fun linkMain(transferPage: Int? = null, isClose: Boolean? = null, context: Context? = null) {
        run(MainLink(transferPage, isClose), context)
    }

    fun linkAliAuthDialog(context: Context? = null) {
        run(AliAuthDialogLink(), context)
    }

    fun linkAliAuth(context: Context? = null) {
        run(AliAuthLink(), context)
    }

    fun linkWeb(url: String, useAliTrade: Boolean = false, aliPage: Int? = null, isAliAuth: Boolean = false, context: Context? = null) {
        run(WebLink(url, useAliTrade, aliPage, isAliAuth), context)
    }


    fun linkSearchContent(searchContent: String? = null, context: Context? = null) {
        run(SearchLink(searchContent), context)
    }

    fun linkSetting(context: Context? = null) {
        run(SettingLink(), context)
    }

    fun linkCash(context: Context? = null) {
        run(CashLink(), context)
    }

    fun linkBalanceDetail(context: Context? = null) {
        run(BalanceDetailLink(), context)
    }

    fun linkCashList(context: Context? = null) {
        run(CashListLink(), context)
    }

    fun linkRN(
        index: String,
        componentName: String,
        directName: String? = null,
        context: Context? = null) {
        run(RNLink(index, componentName, directName), context)
    }

    fun linkPreview(
        url: String,
        transitionView: View? = null,
        context: Context? = null) {
        run(PreviewLink(url, transitionView), context)
    }

    fun run(
        link: BaseLink?,
        context: Context? = null
    ) {
        val constantContext = context?:SysContext.getApp()
        link?.perform(constantContext)
    }

}
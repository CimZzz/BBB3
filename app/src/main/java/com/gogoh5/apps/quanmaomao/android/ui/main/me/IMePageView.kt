package com.gogoh5.apps.quanmaomao.android.ui.main.me

import android.content.Context
import com.gogoh5.apps.quanmaomao.library.base.IView
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean

interface IMePageView: IView {
    fun getContext(): Context
    fun showLoading()
    fun showError()
    fun hideRefreshing()
    fun updateContent(meBean: MeBean)
    fun updateNickName(nickName: String?)
    fun updateShortId(shortId: String?)
    fun updateAvatar(avatar: String?)
}
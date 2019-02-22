package com.gogoh5.apps.quanmaomao.library.base

interface ILazyLoadView<T> : IView {
    fun showLoading()
    fun showError()
    fun showContent(data: T? = null)
}
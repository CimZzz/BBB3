package com.gogoh5.apps.quanmaomao.library.base

import java.io.Serializable

abstract class BaseLazyLoadBean: Serializable {
    companion object {
        const val STATUS_DEFAULT = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_FAILURE = 2

        val EMPTY = object: BaseLazyLoadBean(STATUS_SUCCESS){}
    }
    private var status = STATUS_DEFAULT

    constructor()
    constructor(status: Int) {
        if(status in STATUS_DEFAULT..STATUS_FAILURE)
            this.status = status
    }

    fun isSuccess() = status == STATUS_SUCCESS
    fun isFailure() = status == STATUS_FAILURE


    fun buildSuccess(): BaseLazyLoadBean {
        status = STATUS_SUCCESS
        return this
    }

    fun buildFailure(): BaseLazyLoadBean {
        status = STATUS_FAILURE
        return this
    }
}
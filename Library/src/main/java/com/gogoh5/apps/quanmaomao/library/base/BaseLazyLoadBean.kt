package com.gogoh5.apps.quanmaomao.library.base

import java.io.Serializable

open class BaseLazyLoadBean(val obj: Any? = Unit, private var status: Int = STATUS_DEFAULT): Serializable {
    companion object {
        const val STATUS_DEFAULT = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_FAILURE = 2

        val EMPTY = object: BaseLazyLoadBean(Unit, STATUS_SUCCESS){}
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

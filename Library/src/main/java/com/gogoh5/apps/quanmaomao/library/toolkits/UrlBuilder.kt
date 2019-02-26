package com.gogoh5.apps.quanmaomao.library.toolkits

import com.gogoh5.apps.quanmaomao.library.utils.StringUtils

class UrlBuilder(url: String, val needQuestionMark : Boolean = true) {
    val builder = StringBuilder(url)
    var isFirstParams = true


    fun append(key: String, value: Any?, needEncode: Boolean = true) : UrlBuilder {
        if(value == null)
            return this

        val str = value.toString()
        if(str.isBlank())
            return this

        if(isFirstParams) {
            isFirstParams = false
            if(needQuestionMark)
                builder.append('?')
        }
        else builder.append('&')

        builder.append(key).append('=').append(if(needEncode) StringUtils.urlEncode(str) else value)
        return this
    }


    fun build() = builder.toString()


    operator fun set(key: String, value: Any?) {
        append(key, value)
    }

}
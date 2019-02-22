package com.gogoh5.apps.quanmaomao.library.toolkits

open class UrlPath(
    val path: String,
    open var url: String = ""
) {

    open infix fun with(host: String) {
        url = host + path
    }
}

class CompletedUrlPath(url: String): UrlPath("", url) {
    override fun with(host: String) {
    }
}

fun urlOf(url: String): UrlPath = CompletedUrlPath(url)
fun pathOf(path: String): UrlPath = UrlPath(path)
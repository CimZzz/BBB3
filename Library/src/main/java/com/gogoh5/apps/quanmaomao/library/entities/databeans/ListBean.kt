package com.gogoh5.apps.quanmaomao.library.entities.databeans

import java.io.Serializable

data class ListBean(
    var list: List<*>? = null,
    var over: Boolean = false,
    var pageNum: Int = 0,
    var endTime: Long = 0L
): Serializable
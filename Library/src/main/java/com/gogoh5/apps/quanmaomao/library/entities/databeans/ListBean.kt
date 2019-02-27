package com.gogoh5.apps.quanmaomao.library.entities.databeans

import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import java.io.Serializable

open class ListBean(
    var list: List<BaseRenderer>? = null,
    var over: Boolean = false,
    var pageNum: Int = 0,
    var endTime: Long = 0L
): Serializable
package com.gogoh5.apps.quanmaomao.test.entities.databeans

import java.io.Serializable

data class AliPageBean(
    val pageId: Int,
    val pageName: String
): Serializable {
    override fun toString(): String = pageName
}
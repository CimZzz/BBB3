package com.gogoh5.apps.quanmaomao.android.entities.lazyloadbeen

import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.entities.databeans.MeBean

class MeLazyLoadBean(
    status: Int,
    val meBean: MeBean?
) : BaseLazyLoadBean(status)
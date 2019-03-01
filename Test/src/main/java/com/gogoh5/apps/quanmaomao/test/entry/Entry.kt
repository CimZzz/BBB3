package com.gogoh5.apps.quanmaomao.test.entry

import com.gogoh5.apps.quanmaomao.library.entities.links.WebLink
import com.gogoh5.apps.quanmaomao.library.toolkits.HunterManager
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import com.gogoh5.apps.quanmaomao.library.utils.LogUtils
import com.gogoh5.apps.quanmaomao.test.entities.databeans.AliPageBean
import com.gogoh5.apps.quanmaomao.test.entities.databeans.LogBean
import com.gogoh5.apps.quanmaomao.test.modules.TestModule
import java.lang.ref.WeakReference
import java.util.*

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/2/27 17:41:00
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object Entry {
    val TYPE_LOG_UPDATE = 0

    fun getTest(): TestModule = TestModule.instance

    fun getAliPageArr() = arrayOf(AliPageBean(WebLink.PAGE_ORDER, "百川订单"), AliPageBean(WebLink.PAGE_CART, "百川购物车"))


    private val logList = LinkedList<LogBean>()

    val hunterManager = HunterManager()

    private var isInited: Boolean = false

    fun init() {
        if(!isInited) {
            isInited = true
            LogUtils.hunterManager = HunterManager()
            LogUtils.hunterManager?.register("Entry", RefDataHunter(WeakReference(this)) { entry, params ->
                val logBean = LogBean(params[0] as Int, params[1] as String, params[2] as String)
                entry.addLogBean(logBean)
            })
        }
    }

    private fun addLogBean(logBean: LogBean) {
        logList.add(logBean)
        if(logList.size > 2000) {
            (0 .. 1000).forEach { _ ->
                logList.removeFirst()
            }
        }
        hunterManager.doEat(TYPE_LOG_UPDATE, logBean)
    }

    fun getAllLogBean(): List<LogBean> = logList

    fun getLogBean(type: Int): List<LogBean> = logList.filter { it.logType == type }
}
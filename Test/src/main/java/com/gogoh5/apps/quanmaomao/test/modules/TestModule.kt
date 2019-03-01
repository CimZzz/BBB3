package com.gogoh5.apps.quanmaomao.test.modules

import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.environment.constants.SharedPreferenceType
import com.gogoh5.apps.quanmaomao.library.extended.spdelegate.StringSPDelegate
import com.gogoh5.apps.quanmaomao.library.utils.JSONUtils

class TestModule private constructor(): BaseModule() {
    companion object {
        internal val instance: TestModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { TestModule() }
    }
    private val sharedPreference = getSharePreferences(SharedPreferenceType.TYPE_TEST)

    /*Host 记录*/
    private var hostHistoryArr: String? by StringSPDelegate(sharedPreference, "hostHistoryArr")
    private var hostHistoryList: List<String>? = null
    fun getHostHistoryList(): List<String>? {
        if(hostHistoryList != null)
            return hostHistoryList

        hostHistoryList = JSONUtils.convertJSONArrayToStringList(JSONUtils.parseJSONArray(hostHistoryArr))
        hostHistoryList?.let { ArrayList(it) }

        val list = hostHistoryList as MutableList<String>? ?: ArrayList()
        list.add(Constants.TEST_HOST)
        hostHistoryList = list

        return hostHistoryList
    }

    fun pushHostHistoryList(content: String) {
        if(hostHistoryList == null)
            hostHistoryList = JSONUtils.convertJSONArrayToStringList(JSONUtils.parseJSONArray(hostHistoryArr))


        val tempList: ArrayList<String> = hostHistoryList as ArrayList<String>? ?: ArrayList()
        tempList.remove(content)
        tempList.add(0, content)
        hostHistoryList = tempList
        hostHistoryArr = JSONUtils.convertListToJSONArray(hostHistoryList)?.toJSONString()
    }

    fun clearHostHistory() {
        (hostHistoryList as MutableList<String>?)?.clear()
        hostHistoryList = null
        hostHistoryArr = null
    }


    /*是否启动Test Service*/
    var isOpenTestService: Boolean = false


}
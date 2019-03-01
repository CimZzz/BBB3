package com.gogoh5.apps.quanmaomao.library.environment.modules


import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.environment.constants.SharedPreferenceType
import com.gogoh5.apps.quanmaomao.library.extended.spdelegate.StringSPDelegate
import com.gogoh5.apps.quanmaomao.library.utils.JSONUtils

class DataModule private constructor(): BaseModule() {
    companion object {
        internal val instance: DataModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { DataModule() }
    }
    private val sharedPreference = getSharePreferences(SharedPreferenceType.TYPE_DATA)

    /*搜索历史记录*/
    private var searchHistoryArr: String? by StringSPDelegate(sharedPreference, "searchHistoryArr")
    private var searchHistoryList: List<String>? = null
    fun getSearchHistoryList(): List<String>? {
        if(searchHistoryList != null)
            return searchHistoryList

        if(searchHistoryArr.isNullOrEmpty())
            return null

        searchHistoryList = JSONUtils.convertJSONArrayToStringList(JSONUtils.parseJSONArray(searchHistoryArr))
        return searchHistoryList?.let { ArrayList(it) }
    }

    fun pushSearchHistoryList(content: String) {
        if(searchHistoryList == null)
            searchHistoryList = JSONUtils.convertJSONArrayToStringList(JSONUtils.parseJSONArray(searchHistoryArr))


        val tempList: ArrayList<String> = searchHistoryList as ArrayList<String>? ?: ArrayList()
        tempList.remove(content)
        tempList.add(0, content)
        searchHistoryList = tempList
        searchHistoryArr = JSONUtils.convertListToJSONArray(searchHistoryList)?.toJSONString()
    }

    fun clearSearchHistory() {
        (searchHistoryList as MutableList<String>?)?.clear()
        searchHistoryList = null
        searchHistoryArr = null
    }
}
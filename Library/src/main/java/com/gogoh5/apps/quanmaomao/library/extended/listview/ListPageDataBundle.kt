package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CallSuper
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer

open class ListPageDataBundle() : Parcelable {

    internal var contentState = ListPage.CONTENT_LOADING
    internal var isContentLoading : Boolean = false

    internal var pageNum = 0
    internal var headerList: ArrayList<BaseRenderer> = ArrayList()
    internal var contentList: ArrayList<BaseRenderer> = ArrayList()

    internal var contentFilter: HashSet<String> = HashSet()

    internal var variableMap: HashMap<String, Any?> = HashMap()
    internal var configMap: HashMap<String, Any?> = HashMap()

    @CallSuper
    open fun resetAllData() {
        headerList.clear()
        pageNum = -1
        contentState = ListPage.CONTENT_LOAD
        contentList.clear()
        contentFilter.clear()
        variableMap.clear()
    }

    open fun resetPageData() {
        pageNum = -1
        contentState = ListPage.CONTENT_LOAD
        contentList.clear()
        contentFilter.clear()
    }

    @Synchronized
    fun getVariable(key: String, defaultValue: Any?): Any? {
        val value = variableMap[key]
        if(value == null && defaultValue != null) {
            variableMap[key] = defaultValue
            return defaultValue
        }
        return value
    }

    @Synchronized
    fun setVariable(key: String, any: Any?) {
        variableMap.remove(key)
        variableMap[key] = any
    }

    @Synchronized
    fun getConfig(key: String, defaultValue: Any?): Any? {
        val value = configMap[key]
        if(value == null && defaultValue != null) {
            configMap[key] = defaultValue
            return defaultValue
        }
        return value
    }

    @Synchronized
    fun setConfig(key: String, any: Any?) {
        configMap.remove(key)
        configMap[key] = any
    }

    fun checkAndAddFilter(filterStr: String): Boolean {
        if(contentFilter.contains(filterStr)) {
            return false
        }

        contentFilter.add(filterStr)
        return true
    }


    constructor(parcel: Parcel) : this() {
//        state = parcel.readInt()
//        contentState = parcel.readInt()
//        pageNum = parcel.readInt()
//        headerList = parcel.readArrayList(javaClass.classLoader) as ArrayList<BaseRenderer>
//        contentList = parcel.readArrayList(javaClass.classLoader) as ArrayList<BaseRenderer>
//        variableMap = parcel.readHashMap(javaClass.classLoader) as HashMap<String, Any?>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(state)
//        parcel.writeInt(contentState)
//        parcel.writeInt(pageNum)
//        parcel.writeList(headerList)
//        parcel.writeList(contentList)
//        parcel.writeMap(variableMap)
    }

    final override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ListPageDataBundle> {
        override fun createFromParcel(parcel: Parcel): ListPageDataBundle {
            return ListPageDataBundle(parcel)
        }

        override fun newArray(size: Int): Array<ListPageDataBundle?> {
            return arrayOfNulls(size)
        }
    }
}
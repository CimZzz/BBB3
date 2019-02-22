package com.gogoh5.apps.quanmaomao.library.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/2/15 14:11:44
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object JSONUtils {
    fun convertJSONArrayToStringArray(array: JSONArray?): Array<String>? {
        if(array!= null && array.size != 0)
            return array.filterIsInstance(String::class.java).toTypedArray()
        return null
    }
    fun convertJSONArrayToStringList(array: JSONArray?): List<String>? {
        if(array!= null && array.size != 0) {
            val list = ArrayList<String>()
            array.filterIsInstance(String::class.java).forEach {
                list.add(it)
            }

            return list
        }
        return null
    }

    fun convertListToJSONArray(list: List<*>?): JSONArray? {
        if(list == null)
            return null

        val arr = JSONArray()
        list.forEach {
            arr.add(it)
        }
        return arr
    }

    fun parseJSONArray(content: String?): JSONArray? {
        if(content == null)
            return null
        try {
            return JSON.parseArray(content)
        }
        catch (e: Exception) {
            return null
        }
    }
}
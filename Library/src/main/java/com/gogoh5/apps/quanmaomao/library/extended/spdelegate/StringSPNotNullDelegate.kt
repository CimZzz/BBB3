package com.gogoh5.apps.quanmaomao.library.extended.spdelegate

import android.content.SharedPreferences
import com.gogoh5.apps.quanmaomao.library.extensions.GetResultNotNull
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRun
import kotlin.reflect.KProperty

class StringSPNotNullDelegate(val sharedPreferences: SharedPreferences, val key: String, val defaultBody: GetResultNotNull<String>, val changeBack: TakeRun<String>? = null) {
    var value: String? = null

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        var value = value
        if(value == null) {
            value = sharedPreferences.getString(key, defaultBody())!!
            if(!sharedPreferences.contains(key))
                sharedPreferences.edit().putString(key, value).apply()
        }
        return value
    }

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        if(value != this.value) {
            this.value = value
            val editor = sharedPreferences.edit()
            if (value == null)
                editor.remove(key)
            else editor.putString(key, value).apply()
            changeBack?.let { it(value ?: defaultBody()) }
        }
    }
}
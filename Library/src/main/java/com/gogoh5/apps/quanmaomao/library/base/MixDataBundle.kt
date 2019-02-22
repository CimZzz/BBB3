package com.gogoh5.apps.quanmaomao.library.base

import android.content.Intent
import android.os.Bundle
import com.gogoh5.apps.quanmaomao.library.extensions.TakeMoreRun
import com.gogoh5.apps.quanmaomao.library.extensions.TakeRunAnyResult
import java.io.Serializable

class MixDataBundle(
    val instanceState : Bundle? = null,
    val passIntent : Intent? = null,
    val readable: TakeRunAnyResult<String>? = null,
    private val savable: TakeMoreRun<String, Any?>? = null
) {

    inline fun <reified E> getObject(key : String) : E? {
        var obj: Any?
        obj = instanceState?.getSerializable(key)
        if(obj == null)
            obj = passIntent?.getSerializableExtra(key)

        if(obj == null)
            obj = readable?.let { it(key) }

        if(obj == null)
        return null

        if(E::class.java.isAssignableFrom(obj.javaClass))
            return E::class.java.cast(obj)
        else return null
    }

    inline fun <reified E> getParcelObject(key : String) : E? {
        var obj: Any?
        obj = instanceState?.getParcelable(key)
        if(obj == null)
            obj = passIntent?.getParcelableExtra(key)

        if(obj == null)
            obj = readable?.let { it(key) }

        if(obj == null)
            return null

        if(E::class.java.isAssignableFrom(obj.javaClass))
            return E::class.java.cast(obj)
        else return null
    }

    fun saveObject(key : String, value: Serializable?) {
        when {
            instanceState != null -> instanceState.putSerializable(key, value)
            passIntent != null -> passIntent.putExtra(key, value)
            savable != null -> savable.let { it(key, value) }
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean) : Boolean =
        when {
            instanceState != null -> instanceState.getBoolean(key, defaultValue)
            passIntent != null -> passIntent.getBooleanExtra(key, defaultValue)
            readable != null -> readable.let { it(key)?:defaultValue } as Boolean
            else -> defaultValue
        }

    fun saveBoolean(key: String, value: Boolean) {
        when {
            instanceState != null -> instanceState.putBoolean(key, value)
            passIntent != null -> passIntent.putExtra(key, value)
        }
    }

    fun getString(key: String, defaultValue: String?) : String? =
        when {
            instanceState != null -> instanceState.getString(key, defaultValue)
            passIntent != null -> passIntent.getStringExtra(key)?:defaultValue
            readable != null -> readable.let { it(key)?:defaultValue } as String?
            else -> defaultValue
        }


    fun saveString(key: String, value: String?) {
        when {
            instanceState != null -> instanceState.putString(key, value)
            passIntent != null -> passIntent.putExtra(key, value)
        }
    }

    fun getDouble(key: String, defaultValue: Double): Double =
        when {
            instanceState != null -> instanceState.getDouble(key, defaultValue)
            passIntent != null -> passIntent.getDoubleExtra(key, defaultValue)
            readable != null -> readable.let { it(key)?:defaultValue } as Double
            else -> defaultValue
        }

    fun saveDouble(key: String, value: Double) {
        when {
            instanceState != null -> instanceState.putDouble(key, value)
            passIntent != null -> passIntent.putExtra(key, value)
        }
    }

    fun getInt(key: String, defaultValue: Int): Int =
        when {
            instanceState != null -> instanceState.getInt(key, defaultValue)
            passIntent != null -> passIntent.getIntExtra(key, defaultValue)
            readable != null -> readable.let { it(key)?:defaultValue } as Int
            else -> defaultValue
        }

    fun saveInt(key: String, value: Int) {
        when {
            instanceState != null -> instanceState.putInt(key, value)
            passIntent != null -> passIntent.putExtra(key, value)
        }
    }
}
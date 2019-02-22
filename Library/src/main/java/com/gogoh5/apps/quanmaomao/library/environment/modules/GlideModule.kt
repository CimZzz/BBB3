package com.gogoh5.apps.quanmaomao.library.environment.modules

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class GlideModule private constructor(): BaseModule() {
    companion object {
        internal val instance: GlideModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideModule()
        }
    }
    val glide : RequestManager = Glide.with(SysContext.getApp())

    fun loadNetPicDirectly(url : String?, view : ImageView, error: Int? = null) {
        if(error == null)
            glide.load(url).into(view)
        else glide.load(url).apply(option().placeholder(error).error(error)).into(view)
    }

    fun loadNetPicDirectly(url : String?, default: Int, view : ImageView) {
        if(url.isNullOrEmpty())
            loadResourceDirectly(default, view)
        else loadNetPicDirectly(url, view, default)
    }

    fun loadResourceDirectly(resId: Int, view: ImageView, error: Int? = null) {
        if(error == null)
            glide.load(resId).into(view)
        else glide.load(resId).apply(option().error(error))
    }

    inline fun <reified T> loadNetPicDirectly(url : String?, target : Target<T>) =
        glide.`as`(T::class.java).load(url).into(target)

    inline fun <reified T> loadNetPicDirectly(url : String?, default: Int, target : Target<T>) =
        glide.`as`(T::class.java).load(url).apply(option().error(default)).into(target)

    fun fromUrl(url : String?) = glide.load(url)

    fun option() = RequestOptions()

    fun cancel(view: ImageView) = glide.clear(view)

    fun cancel(target: Target<*>?) = glide.clear(target)

    fun clear() {
        Glide.get(SysContext.getApp()).clearDiskCache()
    }
}
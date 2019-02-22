package com.gogoh5.apps.quanmaomao.library.environment.constants

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.modules.FileModule
import com.gogoh5.apps.quanmaomao.library.utils.logCimZzz
import java.io.File

@GlideModule
class GlideConfig: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val glideCacheFile = SysContext.getFile().getCacheDir(Constants.DIR_GLIDE, FileModule.FLAG_MUST_EXIST)
        logCimZzz("GlideConfig bsdasdasdasd")
        if(glideCacheFile != null)
            builder.setDiskCache(GlideCache(glideCacheFile))
    }

    class GlideCache: DiskLruCacheFactory {
        constructor(file: File) : super(CacheDirectoryGetter { file }, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE.toLong())
    }
}
package com.gogoh5.apps.quanmaomao.library.environment.modules

import android.Manifest
import android.os.Environment
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import java.io.File

class FileModule private constructor() {
    companion object {
        const val FLAG_DEFAULT = 0
        const val FLAG_EXIST = 1
        const val FLAG_MUST_EXIST = 2
        const val FLAG_NEW = 3

        internal val instance: FileModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FileModule()
        }
    }

    private val isAllowUseSDCard: Boolean = SysContext.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)


    fun getRootCacheDir(): File {
        if(isAllowUseSDCard)
            return getDir(getExternalRootDir(), "cache", FLAG_MUST_EXIST)?:SysContext.getApp().cacheDir
        return SysContext.getApp().cacheDir
    }


    fun getCacheDir(fileName: String, flag: Int = FLAG_DEFAULT): File? = getDir(getRootCacheDir(), fileName, flag)

    fun getCacheFile(fileName: String, flag: Int = FLAG_DEFAULT): File? = getFile(getRootCacheDir(), fileName, flag)

    fun getUpgradeFile(fileName: String, flag: Int = FLAG_DEFAULT): File? = getFile(getDir(getRootCacheDir(), "upgradeDir", FLAG_MUST_EXIST), fileName, flag)

    private fun getExternalRootDir(): File? {
        return getDir(Environment.getExternalStorageDirectory(), Constants.EXTERNAL_ROOT)
    }

    private fun getDir(dir: File?, fileName: String, flag: Int = FLAG_DEFAULT): File? {
        if(dir == null)
            return null

        val file = File(dir, fileName)

        when(flag) {
            FLAG_EXIST -> {
                if(!file.exists())
                    return null
            }
            FLAG_MUST_EXIST -> {
                if(!file.exists() && !file.mkdirs())
                    return null
                else if(file.exists() && !file.isDirectory)
                    return null
            }
            FLAG_NEW -> {
                if (file.exists() && !file.delete())
                    return null

                if (!file.mkdirs())
                    return null
            }
            else -> {
                if(file.exists() && !file.isDirectory)
                    return null
            }
        }

        return file
    }

    private fun getFile(dir: File?, fileName: String, flag: Int = FLAG_DEFAULT): File? {
        if(dir == null)
            return null
        val file = File(dir, fileName)

        when(flag) {
            FLAG_EXIST -> {
                if(!file.exists())
                    return null
            }
            FLAG_MUST_EXIST -> {
                if(!file.exists() && !file.createNewFile())
                    return null
            }
            FLAG_NEW -> {
                if (file.exists() && !file.delete())
                    return null

                if (!file.createNewFile())
                    return null
            }
        }

        return file
    }

}

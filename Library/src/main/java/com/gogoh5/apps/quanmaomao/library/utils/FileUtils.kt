package com.gogoh5.apps.quanmaomao.library.utils

import com.gogoh5.apps.quanmaomao.library.extensions.forEach
import java.io.File
import java.lang.Exception

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/2/21 16:40:58
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object FileUtils {


    fun calcByteCount(file: File?): Long {
        try {
            if(file == null)
                return 0L

            if(file.isFile)
                return calcFileByteCount(file)

            var totalByteCount: Long = 0L
            file.forEach {
                totalByteCount += calcByteCount(it)
            }
            return totalByteCount
        }
        catch (e: Exception) {
            return 0L
        }
    }

    private fun calcFileByteCount(file: File): Long {
        return try {
            file.length()
        } catch (e: Exception) {
            0L
        }
    }


    fun delete(file: File?, aboutSelf: Boolean = false) {
        try {
            if (file == null)
                return

            if (file.isFile) {
                deleteFile(file)
                return
            }

            file.forEach {
                delete(it, true)
            }

            if(aboutSelf)
                file.delete()
        }
        catch (e: Exception) {

        }
    }

    private fun deleteFile(file: File) {
        try {
            file.delete()
        }
        catch (e: Exception) {

        }
    }
}
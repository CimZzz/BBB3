package com.gogoh5.apps.quanmaomao.library.utils

import android.text.SpannableStringBuilder
import com.gogoh5.apps.quanmaomao.library.toolkits.TextSpanBuilder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object StringUtils {
    fun formatPrice(price: Double, prefix: String? = null, postfix: String? = null) : String {
        val priceStr = price.toString()
        val pointIdx = priceStr.indexOf(".")

        if(pointIdx == -1)
            return priceStr

        var subIndex = priceStr.length - 1

        if(pointIdx + 3 < priceStr.length )
            subIndex = pointIdx + 2

        do {
            if(priceStr[subIndex] == '0')
                subIndex --
            else break
        } while (subIndex > pointIdx)

        if(subIndex == pointIdx)
            subIndex --


        val afterPriceStr = priceStr.substring(0, subIndex + 1)

        return when {
            prefix != null && postfix != null ->  prefix + afterPriceStr + postfix
            prefix != null -> prefix + afterPriceStr
            postfix != null -> afterPriceStr + postfix
            else -> afterPriceStr
        }
    }

    fun buildBigPrice(priceStr : String, size : Int? = null) : SpannableStringBuilder =
        TextSpanBuilder().append(priceStr, size = size, endIndex = priceStr.indexOf(".")).build()

    fun buildBigPrice(price : Double, size : Int? = null) = buildBigPrice(formatPrice(price), size)


    val YMDDateFormat by lazy (LazyThreadSafetyMode.SYNCHRONIZED){ SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA) }
    val YMD2HMSDateFormat by lazy (LazyThreadSafetyMode.SYNCHRONIZED){ SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA) }
    fun formatDate(time: Long, format: SimpleDateFormat? = null, formatStr : String? = null) : String =
        when {
            format != null -> format.format(time)
            formatStr != null -> SimpleDateFormat(formatStr, Locale.CHINA).format(time)
            else -> ""
        }

    fun urlEncode(str: String, enc: String = "utf-8"): String? {
        return URLEncoder.encode(str, enc)
    }


    fun convertToNull(value: String): String? {
        if(value.isBlank())
            return null
        else return value
    }

    fun formatCount(count: Long): String {
        return count.toString()
    }

    fun formatByteSize(byteSize: Long?): String {
        if(byteSize == null)
            return "未知"

        var postfix = 0
        var tempByteSize = byteSize

        while (tempByteSize >= 1024L) {
            tempByteSize /= 1024L
            postfix ++
            if(postfix >= 3)
                break
        }

        return when(postfix) {
            0 -> "${tempByteSize}B"
            1 -> "${tempByteSize}KB"
            2 -> "${tempByteSize}MB"
            else -> "${tempByteSize}GB"
        }
    }
}
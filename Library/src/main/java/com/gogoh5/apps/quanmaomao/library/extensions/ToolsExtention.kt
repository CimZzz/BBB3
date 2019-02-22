package com.gogoh5.apps.quanmaomao.library.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.support.v4.util.SparseArrayCompat
import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.toolkits.EncryptUtils
import com.gogoh5.apps.quanmaomao.library.toolkits.UrlBuilder
import java.io.File

//------Bitmap
fun Bitmap.sizeMatrix(matrix: Matrix, width: Int, height: Int) {
    val wRatio = width * 1f / getWidth()
    val hRatio = height * 1f / getHeight()

    val ratio = Math.min(wRatio, hRatio)
    matrix.setScale(ratio, ratio)

    val afterWidth = getWidth() * ratio
    val afterHeight = getHeight() * ratio

    matrix.postTranslate((width - afterWidth) / 2, (height - afterHeight) / 2)
}

fun Bitmap.scaleRatio(width: Int, height: Int): Float {
    val wRatio = width * 1f / getWidth()
    val hRatio = height * 1f / getHeight()

    return Math.min(wRatio, hRatio)
}

//------Paint
fun Paint.textHeight(): Float = fontMetrics.bottom - fontMetrics.top
fun Paint.textCenter(): Float = (fontMetrics.bottom + fontMetrics.top) / 2
fun Paint.cutText(text: String, width: Float): Int {
    var realLength = measureText(text)
    if(realLength <= width)
        return -1

    if(text.length <= 1)
        return text.length

    var endIndex: Int = text.length


    do {
        endIndex -= 1
        realLength = measureText(text, 0, endIndex)
    }
    while (realLength > width && endIndex > 0)

    if(endIndex <= 0)
        return text.length
    else return endIndex
}

fun Paint.ignoreText(text: String, width: Float): String {
    var realLength = measureText(text)
    if(realLength <= width)
        return text

    if(text.length <= 3)
        return "..."

    var endIndex: Int = text.length - 3


    do {
        endIndex -= 1
        realLength = measureText(text, 0, endIndex)
    }
    while (realLength > width && endIndex > 0)

    if(endIndex <= 3)
        return "..."
    else return text.substring(0, endIndex) + "..."
}

//------Canvas
fun Canvas.drawCenterText(str: String, startX: Float, startY: Float, endY: Float, paint: Paint) =
    drawText(str, startX, (startY + endY) / 2 - paint.textCenter(), paint)

fun Canvas.drawCenterText(str: String, startX: Float, centerY: Float, paint: Paint) =
    drawText(str, startX, centerY - paint.textCenter(), paint)

//------JSON
fun buildJSONObject(body: TakeRun<JSONObject>) : JSONObject {
    val obj = JSONObject()
    body(obj)
    return obj
}

//------Api
fun buildApiEncode(api: String, body: TakeRun<JSONObject>): ByteArray =
    EncryptUtils.encode(buildApi(api, body).toJSONString(), EncryptUtils.randomKey())

fun buildApi(api: String, body: TakeRun<JSONObject>): JSONObject {
    val apiObj = JSONObject()
    apiObj["act"] = api
    val dataObj = JSONObject()
    body(dataObj)
    apiObj["data"] = dataObj

    return apiObj
}


//------URL
fun buildUrl(url: String, needQuestionMask: Boolean = true, body: TakeRun<UrlBuilder>): String {
    val urlBuilder = UrlBuilder(url, needQuestionMask)
    body(urlBuilder)
    return urlBuilder.build()
}


//------SparseArray
typealias SpArray<T> = SparseArrayCompat<T>


//------File
fun File.forEach(run: TakeRun<File>) {
    if(isDirectory) {
        listFiles().forEach {
            run(it)
        }
    }
}
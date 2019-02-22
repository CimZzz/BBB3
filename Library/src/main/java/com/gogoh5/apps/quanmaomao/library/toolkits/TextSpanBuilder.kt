package com.gogoh5.apps.quanmaomao.library.toolkits

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan

class TextSpanBuilder {
    private val spanBuilder = SpannableStringBuilder()

    fun append(text: String) : TextSpanBuilder {
        spanBuilder.append(text)
        return this
    }

    fun append(text: String, span: CharacterStyle): TextSpanBuilder {
        val str = SpannableString(text)
        str.setSpan(span, 0 , str.length, 0)
        spanBuilder.append(str)
        return this
    }

    fun append(text: String, color: Int? = null, size : Int? = null, endIndex : Int? = null) : TextSpanBuilder {
        val str = SpannableString(text)

        var idx = text.length

        if(endIndex != null && endIndex >= 0 && endIndex <= text.length)
            idx = endIndex

        if(color != null)
            str.setSpan(ForegroundColorSpan(color), 0 , idx, 0)
        if(size != null)
            str.setSpan(AbsoluteSizeSpan(size), 0, idx, 0)

        spanBuilder.append(str)
        return this
    }

    fun build() = spanBuilder
}
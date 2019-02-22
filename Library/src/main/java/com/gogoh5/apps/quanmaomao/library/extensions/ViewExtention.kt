package com.gogoh5.apps.quanmaomao.library.extensions

import android.graphics.Paint
import android.graphics.Typeface
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.toolkits.TextSpanBuilder


/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/12 23:59:05
 *  Project : taoke_android
 *  Since Version : Alpha
 */


//--------View
fun View.setSize(width : Int? = null, height : Int? = null) {
    if(width == null && height == null)
        return

    val layoutParams = this.layoutParams?:return
    if(width != null)
        layoutParams.width = width
    if(height != null)
        layoutParams.height = height
    this.layoutParams = layoutParams
}

fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams? ?:return

    if(left != null)
        layoutParams.leftMargin = left


    if(top != null)
        layoutParams.topMargin = top


    if(right != null)
        layoutParams.rightMargin = right


    if(bottom != null)
        layoutParams.bottomMargin = bottom

    setLayoutParams(layoutParams)
}

fun View.tapWith(name: String? = null, actionSource: ActionSource = ActionSource.DEFAULT, action : TakeRun<View?>) {
    setOnClickListener(SysContext.getActionFactory().genClickListener(name, actionSource, action))

}

fun View.setAppBarLayoutFlag(flags: Int) {
    if(layoutParams is AppBarLayout.LayoutParams) {
        val layoutParams = layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = flags
    }
}

//--------ViewGroup
fun ViewGroup.forEach(body: (View)->Unit ) {
    (0 until childCount).forEach {
        body(getChildAt(it))
    }
}

//--------TextView

fun TextView.orgPriceStyle() {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
    paint.typeface = Typeface.DEFAULT
}

fun TextView.underLineStyle() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
    paint.typeface = Typeface.DEFAULT
}

fun TextView.buildText(action: TakeRun<TextSpanBuilder>) {
    val builder = TextSpanBuilder()
    action(builder)
    text = builder.build()
}



//--------EditText

fun EditText.watcher(name: String? = null, actionSource: ActionSource = ActionSource.DEFAULT, action : TakeRun<Editable?>) {
    addTextChangedListener(SysContext.getActionFactory().genTextWatcher(name, actionSource, action))
}

fun EditText.selectionEnd() {
    setSelection(text?.length?:return)
}



//--------ViewPager
fun ViewPager.pageChange(name: String? = null,
                         actionSource: ActionSource = ActionSource.DEFAULT,
                         selected: PageSelected? = null,
                         stateChanged: PageScrollStateChanged? = null,
                         scrolled: PageScrolled? = null) {
    addOnPageChangeListener(SysContext.getActionFactory().genPageChangeListener(name, actionSource, selected, stateChanged, scrolled))
}
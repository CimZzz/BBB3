package com.gogoh5.apps.quanmaomao.library.extended.listview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gogoh5.apps.quanmaomao.library.R
import com.gogoh5.apps.quanmaomao.library.base.BaseLink
import com.gogoh5.apps.quanmaomao.library.base.BaseRenderer
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.library.widgets.ViewHandler

/**
 *  Anchor : Create by CimZzz
 *  Time : 2018/12/22 16:23:23
 *  Project : taoke_android
 *  Since Version : Alpha
 */
abstract class DefaultListPageContext(context: Context? = null) : ListPageContext<ListPageDataBundle>(context) {
    val isExistFilterBar: Boolean
        get() = false
    override val isContentOnly: Boolean = true
    override val isHeaderOnly: Boolean = false
    override val isAutoHeaderOffset: Boolean = false
    override val isDependentContentRequest: Boolean = true
    override val isExistContentBottom: Boolean = true
    override val isAllowHeaderRefresh: Boolean = true

    override fun generateRefreshHeader(parent: ViewGroup): View? = ViewUtils.inflateView(parent, R.layout.item_list_refresh_header)
    override fun generateBottomStateBar(parent: ViewGroup): ViewHandler? =
        ViewUtils.inflateView(parent, R.layout.item_list_content_bottom)


    override fun checkHeaderRefreshDistance(headerView: View, distance: Float): Float {
        if(distance > headerView.height / 2) {
            (headerView as TextView).text = "松开刷新"
            return headerView.height.toFloat()
        }
        else {
            (headerView as TextView).text = "下拉刷新"
            return 0f
        }
    }

    override fun buildContentList(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(recyclerView.context, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            private val horMargin = SysContext.dp2px(3)
            private val verMargin = SysContext.dp2px(5)
            private val paint= Paint()

            init {
                paint.color = SysContext.getColor(R.color.lightGreyColor)
            }

            override fun onDrawOver(canvas: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.onDrawOver(canvas, parent, state)
                if(canvas == null || parent == null)
                    return;
                (0 until parent.childCount).map { parent.getChildAt(it) }.forEach {
                    val position = parent.getChildAdapterPosition(it)
                    if (position > 0)
                        canvas.drawRect(
                            0f,
                            (it.top - verMargin).toFloat(),
                            canvas.width.toFloat(),
                            it.top.toFloat(),
                            paint
                        )
                }
//                    if(position % 2 == 1) {
//                        if(position > 1)
//                            canvas.drawRect(
//                                0f,
//                                (it.top - verMargin).toFloat(),
//                                canvas.width.toFloat(),
//                                it.top.toFloat(),
//                                paint
//                            )
//                        canvas.drawRect(
//                            (it.left - horMargin * 2).toFloat(),
//                            it.top.toFloat(),
//                            it.left.toFloat(),
//                            it.bottom.toFloat(),
//                            paint
//                        )
//                    }
            }

            override fun getItemOffsets(
                outRect: Rect?,
                view: View?,
                parent: RecyclerView?,
                state: RecyclerView.State?
            ) {
                if(outRect == null || view == null || parent == null)
                    return

                val position = parent.getChildAdapterPosition(view)
                if(position != -1) {
                    if(position > 0)
                        outRect.top = verMargin

//                    if(position % 2 == 0)
//                        outRect.right = horMargin
//                    else outRect.left = horMargin
                }
            }
        })
    }

    override fun onLinkInterceptor(viewType: Int, link: BaseLink?) {
        LinkUtils.run(link, getContext())
    }


    override fun generateDataBundle(): ListPageDataBundle = ListPageDataBundle()


    override fun generateBrandRequest(): BaseRequest<*> = BaseRequest.EMPTY
    override fun onHeaderResult(params: Array<out Any>): List<BaseRenderer>? = null


    override fun onCheckPreload(position: Int, totalCount: Int): Boolean = position >= totalCount - 4
}
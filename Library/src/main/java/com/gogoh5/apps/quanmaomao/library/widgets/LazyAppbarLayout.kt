package com.gogoh5.apps.quanmaomao.library.widgets

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import com.gogoh5.apps.quanmaomao.library.toolkits.LazyLoadToolkit
import java.lang.Exception
import android.support.v4.view.ViewCompat
import com.gogoh5.apps.quanmaomao.library.R


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LazyAppbarLayout : AppBarLayout, CoordinatorLayout.AttachedBehavior {
    val behavior = Behavior()
    private val lazyLoadToolkit = LazyLoadToolkit(this)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        addOnOffsetChangedListener {
                _, verticalOffset ->
            lazyLoadToolkit.onVerticalOffsetChanged(-verticalOffset)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        lazyLoadToolkit.onLayoutChanged()
    }

    fun stopScroll() {
        behavior.overScroller?.abortAnimation()
    }

    fun setAdapter(adapter: LazyLoadToolkit.Adapter?) = lazyLoadToolkit.setAdapter(adapter)

    override fun getBehavior(): CoordinatorLayout.Behavior<*> = behavior

    class Behavior : AppBarLayout.Behavior() {
        var firstGet = true
        lateinit var parent: View
        lateinit var child: AppBarLayout
        var recyclerView : RecyclerView? = null
        var overScroller : OverScroller? = null

        var isAutoOffset: Boolean = false

        private var isFlinging: Boolean = false
        private var shouldBlockNestedScroll: Boolean = false

        override fun layoutDependsOn(parent: CoordinatorLayout, child: AppBarLayout, dependency: View?): Boolean {
            this.parent = parent
            this.child = child
            if(dependency is RecyclerView) {
                if(dependency.id == R.id.listContent)
                    recyclerView = dependency
            }
            return super.layoutDependsOn(parent, child, dependency)
        }

        override fun setTopAndBottomOffset(offset: Int): Boolean {
            var realOffset = offset
            val list = recyclerView
            if(list != null && isAutoOffset) {
                val realHeight = list.height + child.height
                if(realHeight > parent.height) {
                    val distance = parent.height - (realHeight - child.totalScrollRange)
                    if(distance > 0) {
                        val minOffset = distance - child.totalScrollRange
                        if(offset < minOffset)
                            realOffset = minOffset
                    }
                }
                else realOffset = 0
            }
            return super.setTopAndBottomOffset(realOffset)
        }

        override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: AppBarLayout, ev: MotionEvent): Boolean {
            shouldBlockNestedScroll = false
            if (isFlinging)
                shouldBlockNestedScroll = true


            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> stopAppbarLayoutFling(child)  //手指触摸屏幕的时候停止fling事件
            }

            return super.onInterceptTouchEvent(parent, child, ev)
        }

        override fun onTouchEvent(parent: CoordinatorLayout?, child: AppBarLayout, ev: MotionEvent?): Boolean {
            if(ev?.action == MotionEvent.ACTION_DOWN)
                stopAppbarLayoutFling(child)
            if(ev?.action != 3)
                recyclerView?.stopScroll()
            return super.onTouchEvent(parent, child, ev)
        }

        override fun onStartNestedScroll(
            parent: CoordinatorLayout,
            child: AppBarLayout,
            directTargetChild: View,
            target: View,
            nestedScrollAxes: Int,
            type: Int
        ): Boolean {
            stopAppbarLayoutFling(child)
            return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
        }

        override fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout,
            child: AppBarLayout,
            target: View,
            dx: Int,
            dy: Int,
            consumed: IntArray,
            type: Int
        ) {
            if (type == 1)
                isFlinging = true

            if (!shouldBlockNestedScroll)
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

            stopNestedScrollIfNeeded(dy, child, target, type)
        }

        override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: AppBarLayout,
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int
        ) {
            if (!shouldBlockNestedScroll)
                super.onNestedScroll(
                    coordinatorLayout,
                    child,
                    target,
                    dxConsumed,
                    dyConsumed,
                    dxUnconsumed,
                    dyUnconsumed,
                    type
                )


            stopNestedScrollIfNeeded(dyUnconsumed, child, target, type)
        }

        override fun onNestedFling(
            coordinatorLayout: CoordinatorLayout,
            child: AppBarLayout,
            target: View,
            velocityX: Float,
            velocityY: Float,
            consumed: Boolean
        ): Boolean {
            var newConsume = consumed
            if(target == recyclerView) {
                newConsume = velocityY > 0 || (target as RecyclerView).computeVerticalScrollOffset() > 0
            }
            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, newConsume)
        }



        override fun onNestedScrollAccepted(
            coordinatorLayout: CoordinatorLayout,
            child: AppBarLayout,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
        ) {
            if(firstGet && overScroller == null) {
                try {
                    val field = AppBarLayout.Behavior::class.java.superclass.getDeclaredField("mScroller")
                    field.isAccessible = true
                    overScroller = field.get(this) as OverScroller?
                    if(overScroller != null)
                        firstGet = false
                }
                catch (e : Exception) {
                    firstGet = false
                }
            }

            overScroller?.abortAnimation()
            super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
        }

        override fun onStopNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            abl: AppBarLayout,
            target: View,
            type: Int
        ) {
            super.onStopNestedScroll(coordinatorLayout, abl, target, type)
            isFlinging = false
            shouldBlockNestedScroll = false
        }


        private fun stopNestedScrollIfNeeded(dy: Int, child: AppBarLayout, target: View, type: Int) {
            if (type == ViewCompat.TYPE_NON_TOUCH) {
                val currOffset = topAndBottomOffset
                if ((dy < 0 && currOffset == 0) || (dy > 0 && currOffset == -child.totalScrollRange)) {
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
                }
            }
        }

        private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
            //通过反射拿到HeaderBehavior中的flingRunnable变量
            try {
                val headerBehaviorType = this.javaClass.superclass.superclass
                val flingRunnableField = headerBehaviorType.getDeclaredField("mFlingRunnable")
                val scrollerField = headerBehaviorType.getDeclaredField("mScroller")
                flingRunnableField.isAccessible = true
                scrollerField.isAccessible = true

                val flingRunnable = flingRunnableField.get(this) as Runnable?
                val overScroller = scrollerField.get(this) as OverScroller?
                if (flingRunnable != null) {
                    appBarLayout.removeCallbacks(flingRunnable)
                    flingRunnableField.set(this, null)
                }
                if (overScroller != null && !overScroller.isFinished) {
                    overScroller.abortAnimation()
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }


}
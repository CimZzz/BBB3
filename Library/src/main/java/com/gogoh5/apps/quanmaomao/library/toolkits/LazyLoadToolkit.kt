package com.gogoh5.apps.quanmaomao.library.toolkits

import android.graphics.Point
import android.support.annotation.CallSuper
import android.support.v4.util.SparseArrayCompat
import android.view.View
import android.view.ViewGroup
import com.gogoh5.apps.quanmaomao.library.R

import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedList

class LazyLoadToolkit(private val hostView: ViewGroup) {
    private val controllerPositionList: ArrayList<Point> = ArrayList()
    private val visibleController: LinkedList<ViewController<*>> = LinkedList()

    private var indexPoint: Point? = null

    private var isVisible = true
    private var scrollOffset: Int = 0
    private var viewOffset: Int = 0

    private var verticalRange: Int = 0


    fun removeDataAt(position: Int) {
        var count = hostView.childCount
        if (count <= position)
            return

        var child = hostView.getChildAt(position)
        var controller = child.getTag(R.id.viewController) as ViewController<*>
        controller.onDropped(this.hostView)

        hostView.removeViewAt(position)

        count--

        for (i in 0 until count) {
            child = hostView.getChildAt(i)
            controller = child.getTag(R.id.viewController) as ViewController<*>
            controller.position = i
        }
    }

    fun setAdapter(adapter: Adapter?) {
        var shortControllerCacheMap: SparseArrayCompat<LinkedList<ViewController<*>>>? = null
        var shortViewCacheMap: HashMap<String, List<View>>? = null
        var count = hostView.childCount
        if (count != 0) {
            shortControllerCacheMap = SparseArrayCompat()
            shortViewCacheMap = HashMap()
            for (i in 0 until count) {
                val child = hostView.getChildAt(i)
                val controller = child.getTag(R.id.viewController) as ViewController<*>
                controller.onCacheView(shortViewCacheMap)
                controller.onDropped(hostView)

                var controllerList: LinkedList<ViewController<*>>? = shortControllerCacheMap.get(controller.dataType)
                if (controllerList == null) {
                    controllerList = LinkedList()
                    shortControllerCacheMap.put(controller.dataType, controllerList)
                }
                controllerList.add(controller)
            }
            hostView.removeAllViews()
            this.controllerPositionList.clear()
            this.visibleController.clear()
        }
        if (adapter == null)
            return

        adapter.toolkit = this
        count = adapter.itemCount
        if (count == 0)
            return

        if (indexPoint == null)
            indexPoint = Point()

        indexPoint!!.x = -1
        indexPoint!!.y = -1

        for (i in 0 until count) {
            val data = adapter.getData(i)
            val dataType = adapter.getItemDataType(data, i)
            var viewController: ViewController<*>? = null
            if (shortControllerCacheMap != null) {
                val controllerList = shortControllerCacheMap.get(dataType)
                if (controllerList != null) {
                    viewController = controllerList.pollFirst()
                    if (controllerList.size <= 0)
                        shortControllerCacheMap.remove(dataType)
                }
            }

            if (viewController == null)
                viewController = adapter.createViewController(hostView, i, dataType)

            if(viewController == null)
                continue

            viewController.setData(data)
            viewController.position = i
            viewController.dataType = dataType
            viewController.initView(hostView, shortViewCacheMap)
            hostView.addView(viewController.itemView)
        }

        shortControllerCacheMap?.clear()
    }

    fun onLayoutChanged() {
        controllerPositionList.clear()
        val count = hostView.childCount
        for (i in 0 until count) {
            val child = hostView.getChildAt(i)
            controllerPositionList.add(getChildRect(child))
        }

        val height = hostView.measuredHeight

        val parentHeight = if (hostView.parent != null) (hostView.parent as View).measuredHeight else 0
        verticalRange = if (height > parentHeight) parentHeight else height
    }

    fun onVisible() {
        if(isVisible)
            return

        isVisible = true

        onVerticalOffsetChanged(scrollOffset, viewOffset)
    }

    fun onInvisible() {
        if(!isVisible)
            return

        isVisible = false

        for (controller in visibleController)
            if(controller.isVisible)
                controller.onInvisible(hostView)

        visibleController.clear()
    }

    fun onVerticalOffsetChanged(scrollOffset: Int, viewOffset : Int = 0, verticalRange : Int = this.verticalRange) {
        this.scrollOffset = scrollOffset
        this.viewOffset = viewOffset

        if(!isVisible)
            return
        val minOffsetRange = scrollOffset - viewOffset - 300
        val maxOffsetRange = scrollOffset - viewOffset + verticalRange + 300

//        LogUtils.tiwzzz("min : $minOffsetRange , max : $maxOffsetRange")

        if (findVisibleRange(minOffsetRange, maxOffsetRange)) {
            for (controller in visibleController) {
                val position = controller.position
                if (position >= indexPoint!!.x && position <= indexPoint!!.y)
                    continue

                controller.onInvisible(hostView)
            }
            visibleController.clear()
            for (i in indexPoint!!.x..indexPoint!!.y) {
                val child = hostView.getChildAt(i)
                val controller = child.getTag(R.id.viewController) as ViewController<*>
                visibleController.add(controller)
                if (!controller.isVisible)
                    controller.onVisible(hostView)
            }
        }
    }

    private fun findVisibleRange(minOffsetRange: Int, maxOffsetRange: Int): Boolean {
        var startIndex = -1
        var endIndex = -1
        val size = controllerPositionList.size

        var low = 0
        var high = size - 1

        if (size == 0)
            return false

        while (low <= high) {
            val mid = (low + high).ushr(1)
            val point = controllerPositionList[mid]
            if (point.y >= minOffsetRange && point.x <= maxOffsetRange)
                high = mid - 1
            else {
                if (point.y < minOffsetRange) {
                    //check next point
                    val nextIdx = mid + 1
                    if (nextIdx >= size)
                        break

                    val nextPoint = controllerPositionList[nextIdx]
                    if (nextPoint.y >= minOffsetRange && nextPoint.x <= maxOffsetRange) {
                        startIndex = nextIdx
                        break
                    } else
                        low = nextIdx
                } else
                    high = mid - 1
            }
        }

        if (startIndex == -1)
            startIndex = 0

        low = startIndex
        high = size - 1

        while (low <= high) {
            val mid = (low + high).ushr(1)
            val point = controllerPositionList[mid]
            if (point.y >= minOffsetRange && point.x <= maxOffsetRange)
                low = mid + 1
            else {
                if (point.x > maxOffsetRange) {
                    val nextIdx = mid - 1
                    if (nextIdx < 0)
                        break
                    //check next point
                    val nextPoint = controllerPositionList[nextIdx]
                    if (nextPoint.y >= minOffsetRange && nextPoint.x <= maxOffsetRange) {
                        endIndex = nextIdx
                        break
                    } else
                        high = nextIdx
                } else
                    low = mid + 1
            }
        }

        if (endIndex == -1)
            endIndex = size - 1

        if (startIndex == indexPoint!!.x && endIndex == indexPoint!!.y)
            return false

        indexPoint!!.set(startIndex, endIndex)
        return true
    }

    private fun getChildRect(child: View): Point {
        val layoutParams = child.layoutParams as ViewGroup.MarginLayoutParams
        return Point(child.top - layoutParams.topMargin, child.bottom + layoutParams.bottomMargin)
    }

    abstract class Adapter {
        internal lateinit var toolkit : LazyLoadToolkit
        abstract val itemCount: Int

        abstract fun getData(position: Int): Any
        abstract fun getItemDataType(data: Any, position: Int): Int
        abstract fun createViewController(parent: ViewGroup, position: Int, dataType: Int): ViewController<*>?


        fun notifyDataSetChanged() {
            toolkit.setAdapter(this)
        }

        fun getViewController(position: Int): ViewController<*>? {
            if (position >= toolkit.hostView.childCount)
                return null

            val view = toolkit.hostView.getChildAt(position)
            return view.getTag(R.id.viewController) as ViewController<*>
        }

        fun notifyDataChangedAt(position: Int) {
            val view = toolkit.hostView.getChildAt(position)
            val controller = view.getTag(R.id.viewController) as ViewController<*>
            if (controller.isVisible)
                controller.onVisible(toolkit.hostView)
        }

        fun notifyDataChangedAt(viewController: ViewController<*>?) {
            if (viewController != null && viewController.isVisible)
                viewController.onVisible(toolkit.hostView)
        }

        fun notifyRemoveAt(position: Int) {
            toolkit.removeDataAt(position)
        }
    }


    @Suppress("UNCHECKED_CAST")
    abstract class ViewController<T: Any> protected constructor(parent: ViewGroup) {
        private lateinit var data: T
        var dataType: Int = 0
            internal set
        var position: Int = 0
            internal set

        internal var isVisible: Boolean = false
        val itemView: View


        init {
            itemView = generateRootView(parent)
            itemView.setTag(R.id.viewController, this)
        }

        internal fun setData(any: Any) {
            data = any as T
        }

        fun getData() : T = data

        abstract fun generateRootView(parent: ViewGroup): View

        abstract fun initView(parent: ViewGroup, shortViewCacheMap: HashMap<String, List<View>>?)

        @CallSuper
        open fun onVisible(parent: ViewGroup) {
            isVisible = true
        }

        @CallSuper
        open fun onInvisible(parent: ViewGroup) {
            isVisible = false

        }

        @CallSuper
        open fun onCacheView(viewCacheMap: HashMap<String, List<View>>) {
        }

        @CallSuper
        open fun onDropped(parent: ViewGroup) {
            isVisible = false
        }

        fun <E : View> findViewById(id: Int): E {
            return itemView.findViewById(id)
        }
    }
}

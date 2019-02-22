package com.gogoh5.apps.quanmaomao.library.base

import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import java.lang.ref.SoftReference

abstract class BasePageAdapter() : PagerAdapter() {
    private val pageCache = SparseArray<SoftReference<BasePage<*>>>()
    var pageBeanCache = SparseArray<Any?>()
    private var currentPosition : Int? = null

    /**
     * Check where is view from
     */
    final override fun isViewFromObject(view: View, `object`: Any) = (`object` as BasePage<*>).isViewFromObject(view)

    /**
     * Initialize base page from sub-implementation
     */
    final override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pageRef = pageCache[position]
        var page = pageRef?.get()
        if(page == null) {
            page = generateBasePage(position)
            var pageBean : Any? = pageBeanCache[position]
            if (pageBean == null) {
                pageBean = page.generateDataParcelable()

                if(pageBean != Unit)
                    pageBeanCache.put(position, pageBean)
            }
            page.initViewPage(container, pageBean, position)
            pageCache.put(position, SoftReference(page))
        }

        page.attachView(container)

        return page
    }

    /**
     * Destroy base page
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val page = `object` as BasePage<*>
        if(page.viewEnter)
            page.quitPage()
        page.detachView(container)
    }

    /**
     * Process page enter or quit
     */
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
//        LogUtils.cimzzz("currentPosition : $currentPosition, position : $position")
        if(currentPosition == null || currentPosition != position) {
//            LogUtils.cimzzz("position : $position")
            if(currentPosition != null) {
                val page = pageCache.get(currentPosition!!)?.get()
                if(page != null)
                    page.quitPage()
            }
            currentPosition = position
            val currentPage = `object` as BasePage<*>
            currentPage.enterPage()
        }
        super.setPrimaryItem(container, position, `object`)
    }

    /**
     * Don't save any page instance by PageAdapter
     */
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    /**
     * save page instance state
     */
    override fun saveState(): Parcelable? {
        val savedState = PagerAdapterSavedState()
        savedState.savedStateMap = pageBeanCache
        return savedState
    }

    /**
     * restore page instance state
     */
    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        val savedState = state as PagerAdapterSavedState? ?: return
        pageBeanCache = savedState.savedStateMap
    }

    /**
     * destroy all pages
     */
    fun destroy() {
        (0 until pageCache.size()).forEach {
            val pageRef = pageCache.valueAt(it)
            val page = pageRef.get()
            if(page != null)
                page.destroyPage()


            pageRef.clear()
        }

        pageCache.clear()
    }

    /**
     * Generate base page if not exist
     */
    abstract fun generateBasePage(position: Int) : BasePage<*>


    /**
     * Notify data set changed
     */
    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        notifyDataSetUpdate()
    }

    /**
     * Notify data set update
     */
    fun notifyDataSetUpdate(vararg params: Any) {
        val count = pageCache.size()
        (0 until count).forEach {
            val pageRef = pageCache.valueAt(it)
            pageRef.get()?.onChanged(*params)
        }
    }


    class PagerAdapterSavedState() : Parcelable {
        lateinit var savedStateMap : SparseArray<Any?>

        constructor(parcel: Parcel) : this() {
            savedStateMap = parcel.readSparseArray(PagerAdapterSavedState::class.java.classLoader) as SparseArray<Any?>
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeSparseArray(savedStateMap)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<PagerAdapterSavedState> {
            override fun createFromParcel(parcel: Parcel): PagerAdapterSavedState {
                return PagerAdapterSavedState(parcel)
            }

            override fun newArray(size: Int): Array<PagerAdapterSavedState?> {
                return arrayOfNulls(size)
            }
        }

    }
}
package com.gogoh5.apps.quanmaomao.library.base

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bigkoo.svprogresshud.SVProgressHUD

abstract class BaseUI<T : BasePresenter<out IView>> : AppCompatActivity() {
    companion object {
        const val MASK_FROM_INSIDE = "_inside"
        const val MASK_CHECK_CLIP_BOARD = "_checkClipBoard"
        const val MASK_LINK = "_link"
    }

    /**
     * Judge first enter
     */
    private var isFirstEnterFlag = false

    /**
     * Judge enter
     */
    var isEnter = false
        private set

    /**
     * Presenter
     */
    lateinit var presenter : T
        private set

    /**
     * Judge is from inside
     */
    var fromInside: Boolean = false
        private set

    /**
     * Judge check clip board
     */
    var checkClipBoard: Boolean = false
        protected set

    /**
     * Need auto clear edit text focus
     */
    var isAllowAutoClearFocus = false

    /**
     * From link
     */
    var fromLink: BaseLink? = null
        internal set

    /**
     * Wait dialog
     */
    var waitDialog: SVProgressHUD? = null

    final override fun onCreate(savedInstanceState: Bundle?) {
        val mixDataBundle = MixDataBundle(savedInstanceState, intent)
        beforeCreate(mixDataBundle)
        super.onCreate(savedInstanceState)

        presenter = initPresenter()
        fromInside = mixDataBundle.getBoolean(MASK_FROM_INSIDE, false)
        checkClipBoard = mixDataBundle.getBoolean(MASK_CHECK_CLIP_BOARD, true)
        fromLink = mixDataBundle.getObject(MASK_LINK)
        if(checkCondition(mixDataBundle)) {
            initView()
            checkDataBundle(mixDataBundle)
            presenter.initPresenter(savedInstanceState, intent)
        }
        else closeDirectly()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.viewSaveInstance(outState?:return)
        val dataBundle = MixDataBundle(outState)
        dataBundle.saveBoolean(MASK_FROM_INSIDE, fromInside)
        dataBundle.saveBoolean(MASK_CHECK_CLIP_BOARD, checkClipBoard)
        dataBundle.saveObject(MASK_LINK, fromLink)
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.viewRestoreInstance(savedInstanceState?:return)
    }

    override fun onStop() {
        super.onStop()
        isEnter = false
        presenter.viewQuit()
    }

    @CallSuper
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if(!isEnter) {
                isEnter = true
                if(!isFirstEnterFlag) {
                    presenter.viewEnter(true)
                    isFirstEnterFlag = true
                }
                else presenter.viewEnter(false)
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }

    override fun finish() {
        if(!fromInside) {
            NavUtils.getParentActivityIntent(this)?.let {
                if (NavUtils.shouldUpRecreateTask(this, it) || isTaskRoot) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(it).startActivities()
                } else
                    NavUtils.navigateUpFromSameTask(this)
            }
        }
        super.finish()
    }


    val boundRect = Rect()
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if(isAllowAutoClearFocus) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val view = window.currentFocus
                    if (view != null && view is EditText) {
                        view.getGlobalVisibleRect(boundRect)
                        if (!boundRect.contains(event.x.toInt(), event.y.toInt())) {
                            val inputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
                            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                            view.clearFocus()
                            return super.dispatchTouchEvent(event)
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        presenter.newIntent(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onDestroy() {
        presenter.viewDestroy()
        closeWaitDialog()
        super.onDestroy()
    }

    open fun beforeCreate(mixDataBundle: MixDataBundle) {

    }

    open fun checkCondition(mixDataBundle: MixDataBundle): Boolean = true

    open fun checkDataBundle(mixDataBundle: MixDataBundle) {

    }

    abstract fun initPresenter() : T
    abstract fun initView()


    fun showWaitDialog(txt: String) {
        closeWaitDialog()

        waitDialog = SVProgressHUD(this)
        waitDialog?.showWithStatus(txt)
    }

    fun closeWaitDialog() {
        waitDialog?.dismissImmediately()
        waitDialog = null
    }

    open fun closeDirectly() {
        finish()
    }

    fun getContext(): Context {
        return this
    }

    fun getPassLink(): BaseLink? {
        return fromLink
    }
}
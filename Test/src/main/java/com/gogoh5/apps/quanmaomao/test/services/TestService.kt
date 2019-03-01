package com.gogoh5.apps.quanmaomao.test.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import com.gogoh5.apps.quanmaomao.library.widgets.FloatView
import com.gogoh5.apps.quanmaomao.test.R
import com.gogoh5.apps.quanmaomao.test.entities.databeans.LogBean
import com.gogoh5.apps.quanmaomao.test.entities.links.BootStrapLink
import com.gogoh5.apps.quanmaomao.test.entry.Entry
import kotlinx.android.synthetic.main.test_log_panel.view.*
import kotlinx.android.synthetic.main.test_main_menu.view.*
import java.lang.ref.WeakReference

class TestService: Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        Entry.getTest().isOpenTestService = true
        attachFloatButton()
    }

    override fun onDestroy() {
        super.onDestroy()

        Entry.getTest().isOpenTestService = false
        detachFloatButton()
        detachMainMenu()
        detachLogPanel()
    }


    private var floatBall: FloatView? = null
    private fun attachFloatButton() {
        val rootView: View = floatBall?:{
            val layoutParams = WindowManager.LayoutParams()
            if(SysContext.checkSDKVersion(Build.VERSION_CODES.O))
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE

            val size = SysContext.dp2px(45)

            layoutParams.width = size
            layoutParams.height = size
            layoutParams.gravity = Gravity.START or Gravity.TOP
            layoutParams.format = PixelFormat.RGBA_8888

            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            val view = FloatView(this)
            view.setBackgroundResource(R.drawable.bg_ball)
            view.callback = object: FloatView.Callback {
                override fun onTouched() {
                    floatBall?.isSelected = false
                    detachMainMenu()
                }
            }

            view.tapWith {
                view.scrollToRightBottom {
                    floatBall?.isSelected = true
                    attachMainMenu()
                }
            }

            view.layoutParams = layoutParams

            floatBall = view
            view
        }()


        if(rootView.parent != null)
            return

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(rootView, rootView.layoutParams)
    }

    private fun detachFloatButton() {
        val view = floatBall?:return
        if(view.parent == null)
            return
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeViewImmediate(view)
    }

    private var mainMenuView: View? = null
    private fun attachMainMenu() {
        val rootView = mainMenuView?: {
            val genView: View = LayoutInflater.from(this).inflate(R.layout.test_main_menu, null)

            val layoutParams = WindowManager.LayoutParams()
            if(SysContext.checkSDKVersion(Build.VERSION_CODES.O))
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE

            val size = SysContext.dp2px(45)

            layoutParams.width = SysContext.getScreenWidth() - size
            layoutParams.height = size
            layoutParams.gravity = Gravity.START or Gravity.BOTTOM
            layoutParams.format = PixelFormat.RGBA_8888
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            genView.layoutParams = layoutParams

            genView.homeBtn.tapWith {
                detachMainMenu()
                SysContext.quit()
                LinkUtils.run(BootStrapLink())
            }

            genView.logPanelBtn.tapWith {
                detachMainMenu()
                if(logPanel?.parent != null) {
                    detachLogPanel()
                    genView.logPanelBtn.text = "打开日志面板"
                }
                else {
                    attachLogPanel()
                    genView.logPanelBtn.text = "关闭日志面板"
                }
            }

            genView.closeServiceBtn.tapWith {
                detachMainMenu()
                stopSelf()
            }


            mainMenuView = genView
            genView
        }()

        if(rootView.parent != null)
            return

        if(logPanel?.parent != null)
            rootView.logPanelBtn.text = "关闭日志面板"
        else rootView.logPanelBtn.text = "打开日志面板"

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(rootView, rootView.layoutParams)
    }

    private fun detachMainMenu() {
        val view = mainMenuView?:return
        if(view.parent == null)
            return
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeViewImmediate(view)
    }



    private var logPanel: View? = null
    private fun attachLogPanel() {
        val rootView: View = logPanel?:{
            val genView: View = LayoutInflater.from(this).inflate(R.layout.test_log_panel, null)

            val layoutParams = WindowManager.LayoutParams()
            if(SysContext.checkSDKVersion(Build.VERSION_CODES.O))
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE

            layoutParams.width = SysContext.getScreenWidth()
            layoutParams.height = SysContext.getScreenHeight() / 2
            layoutParams.gravity = Gravity.TOP or Gravity.START
            layoutParams.format = PixelFormat.RGBA_8888
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            genView.layoutParams = layoutParams


            genView.logTypeSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayOf("全部", "CimZzz", "QMMRequest"))
            genView.logTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    genView.logContentTxt.text = null
                    if(position == 0) {
                        Entry.getAllLogBean().forEach {
                            genView.logContentTxt.append("[${it.logTypeStr}]:${it.logMsg}\n")
                        }
                    }
                    else {
                        Entry.getLogBean(position).forEach {
                            genView.logContentTxt.append("[${it.logTypeStr}]:${it.logMsg}\n")
                        }
                    }
                }
            }

            genView.closeBtn.tapWith {
                detachLogPanel()
            }

            genView.alphaBtn.tapWith {
                if(genView.alpha != 1f) {
                    genView.alpha = 1f
                    genView.alphaBtn.text = "半透明"
                }
                else {
                    genView.alpha = 0.7f
                    genView.alphaBtn.text = "取消半透明"
                }
            }


            logPanel = genView
            genView
        }()

        if(rootView.parent != null)
            return


        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(rootView, rootView.layoutParams)

        detachFloatButton()
        attachFloatButton()

        if(rootView.alpha != 1f)
            rootView.alphaBtn.text = "取消半透明"
        else rootView.alphaBtn.text = "半透明"

        Entry.hunterManager.register("Test_LogPanel", RefDataHunter(WeakReference(this)) {
            service, params ->
            when (params[0]) {
                Entry.TYPE_LOG_UPDATE -> {
                    val view = service.logPanel?:return@RefDataHunter
                    val logBean = params[1] as LogBean
                    val selectId = view.logTypeSpinner.selectedItem as Int
                    if(selectId == 0 || selectId == logBean.logType)
                        view.logContentTxt.append("[${logBean.logTypeStr}]:${logBean.logMsg}\n")
                }
            }
        })

        rootView.logContentTxt.text = null
        Entry.getAllLogBean().forEach {
            rootView.logContentTxt.append("[${it.logTypeStr}]:${it.logMsg}\n")
        }
    }

    private fun detachLogPanel() {
        val view = logPanel?:return
        if(view.parent == null)
            return
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeViewImmediate(view)

        Entry.hunterManager.unregister("Test_LogPanel")
    }
}
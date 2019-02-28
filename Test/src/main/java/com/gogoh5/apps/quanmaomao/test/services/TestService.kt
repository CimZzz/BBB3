package com.gogoh5.apps.quanmaomao.test.services

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.widgets.FloatView

class TestService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initFloatButton()
    }



    private fun initFloatButton() {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams()
        if(SysContext.checkSDKVersion(Build.VERSION_CODES.O))
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE

        layoutParams.width = 120
        layoutParams.height = 120
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.format = PixelFormat.TRANSLUCENT
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        val view = FloatView(this)
        view.setBackgroundColor(Color.BLACK)
        windowManager.addView(view, layoutParams)

        view.moveTo(50, SysContext.getScreenHeight() - 200)
    }
}
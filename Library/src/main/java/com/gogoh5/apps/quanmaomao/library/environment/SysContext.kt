package com.gogoh5.apps.quanmaomao.library.environment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.webkit.WebView
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.gogoh5.apps.quanmaomao.library.environment.modules.*
import com.gogoh5.apps.quanmaomao.library.events.DefaultEventFactory
import com.gogoh5.apps.quanmaomao.library.interfaces.IFactory

object SysContext {
    fun getActionFactory(): IFactory = DefaultEventFactory

    fun getApp(): CustomApplication = CustomApplication.instance
    fun getHttp(): HttpModule = HttpModule.instance
    fun getGlide(): GlideModule = GlideModule.instance
    fun getSetting(): SettingModule = SettingModule.instance
    fun getThread(): ThreadModule = ThreadModule.instance
    fun getUser(): UserModule = UserModule.instance
    fun getWeChat(): WeChatModule = WeChatModule.instance
    fun getData(): DataModule = DataModule.instance
    fun getBus(): BusModule = BusModule.instance
    fun getFile(): FileModule = FileModule.instance



    fun getResources() = getApp().resources!!

    fun getScreenWidth() = getApp().resources.displayMetrics.widthPixels

    fun getScreenHeight() = getApp().resources.displayMetrics.heightPixels

    fun getDensity() = getApp().resources.displayMetrics.density

    fun getDensityDpi() = getApp().resources.displayMetrics.densityDpi

    fun getScaledDensity() = getApp().resources.displayMetrics.scaledDensity

    fun getResourceDp(dpRes: Int) = getResources().getDimension(dpRes)

    fun getResourceSp(spRes: Int) = getResources().getDimension(spRes)


    @Suppress("DEPRECATION")
    fun getColor(color: Int, theme: Resources.Theme? = null) : Int =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getResources().getColor(color, theme)
        else getResources().getColor(color)

    fun getString(resId : Int) = getResources().getString(resId)?:""

    fun getAnimation(resId: Int) = AnimationUtils.loadAnimation(getApp(), resId)

    fun makeNavigationTransparent(activity: Activity, isLightBar : Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<ViewGroup>(android.R.id.content)
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as View
                rootView.fitsSystemWindows = false
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            val decorView = activity.window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLightBar)
                    decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else decorView.systemUiVisibility = decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = Color.TRANSPARENT
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val attributes = activity.window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            activity.window.attributes = attributes
        }
    }

    fun getNavigationBarHeight() : Int {
        val resources = getResources()
        var resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (resourceId > 0) {
            if (resources.getBoolean(resourceId)) {//导航栏是否显示
                resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    return resources.getDimensionPixelSize(resourceId)//获取高度
                }
            }
        }
        return 0
    }

    fun getStatusBarHeight() : Int {
        val resources = getResources()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if(resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }


    fun dp2px(dp : Int) : Int = (getDensity() * dp + 0.5f).toInt()

    fun sp2px(sp : Int) : Int = (getScaledDensity() * sp + 0.5f).toInt()

    fun configWebView(webView: WebView?) {
        if(webView != null) {
            val setting = webView.settings
            setting.domStorageEnabled = true
            setting.javaScriptEnabled = true
            setting.javaScriptCanOpenWindowsAutomatically = true
        }
    }

    fun checkAliAuth(): Boolean {
        return AlibcLogin.getInstance().isLogin
    }

    fun checkSDKVersion(code: Int): Boolean = Build.VERSION.SDK_INT >= code

    fun checkPermission(permission: String): Boolean {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return PermissionChecker.checkSelfPermission(SysContext.getApp(), permission) == PackageManager.PERMISSION_GRANTED
        else return ContextCompat.checkSelfPermission(SysContext.getApp(), permission) == PackageManager.PERMISSION_GRANTED
    }
}
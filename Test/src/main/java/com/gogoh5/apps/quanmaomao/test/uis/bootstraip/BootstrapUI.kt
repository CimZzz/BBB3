package com.gogoh5.apps.quanmaomao.test.uis.bootstraip

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.selectionEnd
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ToastUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.test.R
import com.gogoh5.apps.quanmaomao.test.services.TestService
import kotlinx.android.synthetic.main.ui_bootstrap.*

class BootstrapUI: BaseUI<BootStrapPresenter>(), IBootStrapView {
    var isForceOverlay: Boolean = false

    override fun initPresenter(): BootStrapPresenter = BootStrapPresenter(this)

    override fun initView() {
        isAllowAutoClearFocus = true
        setContentView(R.layout.ui_bootstrap)

        backBtn.tapWith {
            closeDirectly()
        }

        permissionBtn.tapWith {
            doPermission()
        }


        initUid()


        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M)) {
            val ops = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            ops.startWatchingMode(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, packageName) { op, name ->
                if(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW == op &&
                    packageName == name &&
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    isForceOverlay = true
                    checkEnable()
                }
            }
        }
    }

    override fun checkEnable() {
        var canOverlays = true

        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M)) {
            canOverlays = if(isForceOverlay)
                isForceOverlay
            else Settings.canDrawOverlays(this)
        }
        if(canOverlays) {
            permissionBtn.visibility = View.GONE
            permissionStatusTxt.text = "已获取"
            permissionStatusTxt.setTextColor(Color.GREEN)

            ViewUtils.setViewEnable(configContainer, true)
            startService(Intent(this, TestService::class.java))
        }
        else {
            permissionBtn.visibility = View.VISIBLE
            permissionStatusTxt.text = "未获取"
            permissionStatusTxt.setTextColor(Color.RED)

            ViewUtils.setViewEnable(configContainer,false)
        }
    }


    private fun doPermission() {
        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
            return
        }
    }



    private fun initUid() {

        uidContentTxt.setText(SysContext.getUser().uid)
        uidContentTxt.selectionEnd()

        uidResetBtn.tapWith {
            uidContentTxt.setText(SysContext.getUser().uid)
            uidContentTxt.selectionEnd()
        }

        uidCopyBtn.tapWith {
            if(SysContext.copy(uidContentTxt.text.toString()))
                ToastUtils.sendToast("复制成功")
            else ToastUtils.sendToast("复制失败")
        }

        uidPasteBtn.tapWith {
            uidContentTxt.setText(SysContext.paste())
            uidContentTxt.selectionEnd()
        }

        uidClearBtn.tapWith {
            uidContentTxt.text = null
        }

        uidUpdateBtn.tapWith {
            SysContext.getUser().uid = uidContentTxt.text.toString()
            ToastUtils.sendToast("修改成功")
        }
    }
}

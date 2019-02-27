package com.gogoh5.apps.quanmaomao.test.uis.bootstraip

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.View
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.test.R
import kotlinx.android.synthetic.main.ui_bootstrap.*

class BootstrapUI: BaseUI<BootStrapPresenter>(), IBootStrapView {
    override fun initPresenter(): BootStrapPresenter = BootStrapPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_bootstrap)

        permissionBtn.tapWith {
            doPermission()
        }
    }

    override fun checkEnable() {
        var canOverlays = true
        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M))
            canOverlays = Settings.canDrawOverlays(this)
        if(canOverlays && SysContext.checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            permissionBtn.visibility = View.GONE
            permissionStatusTxt.text = "已获取"
            permissionStatusTxt.setTextColor(Color.GREEN)

            ViewUtils.setViewEnable(configContainer, true)
        }
        else {
            permissionBtn.visibility = View.VISIBLE
            permissionStatusTxt.text = "未获取"
            permissionStatusTxt.setTextColor(Color.RED)
        }
    }


    private fun doPermission() {

        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M)) {
            if(!Settings.canDrawOverlays(this)) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
                return
            }
        }


        if(!SysContext.checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
                101
            )
            return
        }

        checkEnable()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 101 && permissions.size == 1) {
            if(permissions[0] == Manifest.permission.SYSTEM_ALERT_WINDOW) {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doPermission()
                }
                else presenter.sendToast("权限授权失败")
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
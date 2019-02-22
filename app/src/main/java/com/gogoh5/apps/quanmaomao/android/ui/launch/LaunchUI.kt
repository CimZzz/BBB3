package com.gogoh5.apps.quanmaomao.android.ui.launch

import android.Manifest
import android.support.v4.app.ActivityCompat
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseLazyLoadBean
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class LaunchUI: BaseUI<LaunchPresenter>(), ILaunchView {
    companion object {
        const val LAZY_LOAD_TIME_OUT = 0
        const val LAZY_LOAD_PERMISSION = 1

        const val REQ_PERMISSION = 100

        const val TIME_OUT = 1500L
    }

    override fun initPresenter(): LaunchPresenter = LaunchPresenter(this)

    override fun initView() {
        setContentView(R.layout.ui_launch)
        val permissionList = ArrayList<String>()
        if(!SysContext.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(!SysContext.checkPermission(Manifest.permission.READ_PHONE_STATE))
            permissionList.add(Manifest.permission.READ_PHONE_STATE)

        if(permissionList.isEmpty())
            presenter.putLazyLoadParams(LAZY_LOAD_PERMISSION, BaseLazyLoadBean.EMPTY)
        else ActivityCompat.requestPermissions(this,
            permissionList.toTypedArray(),
            REQ_PERMISSION)
    }


    override fun closeDirectly() {
        super.finish()
    }

    override fun finish() {
        super.finish()
        SysContext.getThread().post {
            Runtime.getRuntime().exit(0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQ_PERMISSION)
            presenter.putLazyLoadParams(LAZY_LOAD_PERMISSION, BaseLazyLoadBean.EMPTY)
    }
}
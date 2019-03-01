package com.gogoh5.apps.quanmaomao.test.uis.bootstraip

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.extensions.selectionEnd
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.utils.FileUtils
import com.gogoh5.apps.quanmaomao.library.utils.LinkUtils
import com.gogoh5.apps.quanmaomao.library.utils.ToastUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.test.R
import com.gogoh5.apps.quanmaomao.test.entities.databeans.AliPageBean
import com.gogoh5.apps.quanmaomao.test.entry.Entry
import com.gogoh5.apps.quanmaomao.test.services.TestService
import kotlinx.android.synthetic.main.ui_bootstrap.*
import java.lang.Exception
import java.lang.ref.WeakReference

class BootstrapUI: BaseUI<BootStrapPresenter>(), IBootStrapView {
    var isForceOverlay: Boolean = false

    override fun initPresenter(): BootStrapPresenter = BootStrapPresenter(this)

    override fun initView() {
        isAllowAutoClearFocus = true
        Entry.init()
        setContentView(R.layout.ui_bootstrap)

        backBtn.tapWith {
            closeDirectly()
            SysContext.quitApp(200)
        }

        permissionBtn.tapWith {
            doPermission()
        }

        enterBtn.tapWith {
            LinkUtils.linkLaunch(this)
            closeDirectly()
        }

        initClearAll()
        initSettingBtn()
        initFloatSwitch()
        initUid()
        initHost()
        initUrl()
        initAliPage()
        initScheme()
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
            ViewUtils.setViewEnable(bottomContainer, true)
        }
        else {
            permissionBtn.visibility = View.VISIBLE
            permissionStatusTxt.text = "未获取"
            permissionStatusTxt.setTextColor(Color.RED)

            ViewUtils.setViewEnable(configContainer,false)
            ViewUtils.setViewEnable(bottomContainer, false)
        }
    }


    private fun doPermission() {
        if(SysContext.checkSDKVersion(Build.VERSION_CODES.M)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
            return
        }
    }

    private fun initClearAll() {
        if(clearAllBtn.getTag(R.id.data) != true) {
            clearAllBtn.setTag(R.id.data, true)
            clearAllBtn.tapWith {
                showWaitDialog("请稍后")
                SysContext.getThread().sendRunnableWithHunter(RefDataHunter(WeakReference(this)) {
                    ui, params ->
                    ui.closeDirectly()
                    ui.closeWaitDialog()
                    try {
                        Runtime.getRuntime().exec("pm clear $packageName")
                    }
                    catch (e: Exception) {

                    }
                }) {
                    try {
                        SysContext.getGlide().clear()
                        FileUtils.delete(SysContext.getFile().getRootCacheDir())
                    }
                    catch (e: Exception) {
                    }
                }
            }
        }
    }

    private fun initSettingBtn() {
        if(settingBtn.getTag(R.id.data) != true) {
            settingBtn.setTag(R.id.data, true)
            settingBtn.tapWith {
                val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
                startActivity(intent)
            }

            settingSelfBtn.tapWith {
                Intent()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                startActivity(intent)
            }
        }
    }

    private fun initFloatSwitch() {
        if(floatSwitchBtn.getTag(R.id.data) != true) {
            floatSwitchBtn.setTag(R.id.data, true)
            floatSwitchBtn.setEnableEffect(true)
            floatSwitchBtn.isChecked = Entry.getTest().isOpenTestService
            floatSwitchBtn.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked)
                    startService(Intent(this, TestService::class.java))
                else stopService(Intent(this, TestService::class.java))
            }
        }
    }


    private fun initUid() {
        if(uidContentTxt.getTag(R.id.data) != true) {
            uidContentTxt.setTag(R.id.data, true)
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

        uidContentTxt.setText(SysContext.getUser().uid)
        uidContentTxt.selectionEnd()
    }

    private fun initHost() {
        if(hostContentTxt.getTag(R.id.data) != true) {
            hostContentTxt.setTag(R.id.data, true)

            hostContentTxt.setText(Constants.HOST)
            hostContentTxt.selectionEnd()
            hostContentTxt.setAdapter(
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    Entry.getTest().getHostHistoryList()
                )
            )

            hostResetBtn.tapWith {
                hostContentTxt.setText(Constants.HOST)
                hostContentTxt.selectionEnd()
            }

            hostCopyBtn.tapWith {
                if (SysContext.copy(hostContentTxt.text.toString()))
                    ToastUtils.sendToast("复制成功")
                else ToastUtils.sendToast("复制失败")
            }

            hostPasteBtn.tapWith {
                hostContentTxt.setText(SysContext.paste())
                hostContentTxt.selectionEnd()
            }

            hostClearBtn.tapWith {
                hostContentTxt.text = null
            }

            hostClearAllBtn.tapWith {
                Entry.getTest().clearHostHistory()
                hostContentTxt.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        Entry.getTest().getHostHistoryList()
                    )
                )
            }

            hostUpdateBtn.tapWith {
                val hostStr = hostContentTxt.text.toString()
                if (hostStr.matches(Regex("^http[s]?://[a-zA-Z0-9:./]*[a-zA-Z0-9]\$"))) {
                    Constants.checkHost(hostStr)
                    Entry.getTest().pushHostHistoryList(hostStr)
                    hostContentTxt.setAdapter(
                        ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1,
                            Entry.getTest().getHostHistoryList()
                        )
                    )
                    ToastUtils.sendToast("修改成功")
                } else ToastUtils.sendToast("Host不合法")
            }
        }
    }


    private fun initUrl() {
        if(urlContentTxt.getTag(R.id.data) != true) {
            urlContentTxt.setTag(R.id.data, true)

            urlClearBtn.tapWith {
                urlContentTxt.text = null
            }

            urlCopyBtn.tapWith {
                if (SysContext.copy(urlContentTxt.text.toString()))
                    ToastUtils.sendToast("复制成功")
                else ToastUtils.sendToast("复制失败")
            }

            urlPasteBtn.tapWith {
                urlContentTxt.setText(SysContext.paste())
                urlContentTxt.selectionEnd()
            }

            urlOpenBtn.tapWith {
                val url = urlContentTxt.text.toString()
                if (url.matches(Regex("^http[s]?://[a-zA-Z0-9:./]*[a-zA-Z0-9]\$")))
                    LinkUtils.linkWeb(url, context = this)
                else presenter.sendToast("Url 地址必须为 http 地址")
            }

            urlAliOpenBtn.tapWith {
                val url = urlContentTxt.text.toString()
                if (url.matches(Regex("^http[s]?://[a-zA-Z0-9:./]*[a-zA-Z0-9]\$")))
                    LinkUtils.linkWeb(url, useAliTrade = true, context = this)
                else presenter.sendToast("Url 地址必须为 http 地址")
            }
        }
    }

    private fun initAliPage() {
        if(aliPageOpenBtn.getTag(R.id.data) != true) {
            aliPageOpenBtn.setTag(R.id.data, true)
            aliPageSpinner.adapter = ArrayAdapter<AliPageBean>(this, android.R.layout.simple_list_item_1, Entry.getAliPageArr())

            aliPageOpenBtn.tapWith {
                val aliPageBean = aliPageSpinner.selectedItem as AliPageBean
                LinkUtils.linkWeb("", aliPage = aliPageBean.pageId, context = this)
            }
        }
    }

    private fun initScheme() {
        if(schemeContentTxt.getTag(R.id.data) != true) {
            schemeContentTxt.setTag(R.id.data, true)

            schemeClearBtn.tapWith {
                schemeContentTxt.text = null
            }

            schemeCopyBtn.tapWith {
                if (SysContext.copy(schemeContentTxt.text.toString()))
                    ToastUtils.sendToast("复制成功")
                else ToastUtils.sendToast("复制失败")
            }

            schemePasteBtn.tapWith {
                schemeContentTxt.setText(SysContext.paste())
                schemeContentTxt.selectionEnd()
            }

            schemeOpenBtn.tapWith {
                val scheme = schemeContentTxt.text.toString()
                LinkUtils.linkScheme(scheme, "不存在指定的Scheme", this)
            }
        }
    }
}

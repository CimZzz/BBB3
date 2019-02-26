package com.gogoh5.apps.quanmaomao.library.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.gogoh5.apps.quanmaomao.library.BuildConfig
import com.gogoh5.apps.quanmaomao.library.entities.links.RNLink

open class BaseRNUI: AppCompatActivity(), DefaultHardwareBackBtnHandler {

    lateinit var mReactRootView: ReactRootView
    lateinit var mReactInstanceManager: ReactInstanceManager

    /**
     * From link
     */
    var fromLink: RNLink? = null
        internal set

    @Override
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        val mixDataBundle = MixDataBundle(savedInstanceState, intent)
        fromLink = mixDataBundle.getObject(BaseUI.MASK_LINK)
        if(fromLink == null) {
            finish()
            return
        }

        mReactRootView = ReactRootView (this)
        val builder = ReactInstanceManager.builder()
        builder.setApplication(application)
            .setBundleAssetName("index.android.bundle")
            .addPackage(MainReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
        build(builder)
        mReactInstanceManager = builder.build()
        // 注意这里的MyReactNativeApp必须对应“index.js”中的
        // “AppRegistry.registerComponent()”的第一个参数
        mReactRootView.startReactApplication(mReactInstanceManager, getReactComponentName(), null)

        setContentView(mReactRootView)
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val dataBundle = MixDataBundle(outState)
        dataBundle.saveObject(BaseUI.MASK_LINK, fromLink)
    }



    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }


    open fun build(builder: ReactInstanceManagerBuilder) {
        builder.setJSMainModulePath(fromLink?.index)
    }
    open fun getReactComponentName(): String? = fromLink?.componentName
}
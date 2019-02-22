package com.gogoh5.apps.quanmaomao.library.environment.modules

import com.gogoh5.apps.quanmaomao.library.base.BaseModule
import com.gogoh5.apps.quanmaomao.library.base.BaseRequest
import com.gogoh5.apps.quanmaomao.library.entities.databeans.InitBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.requests.InitMobileRequest
import com.gogoh5.apps.quanmaomao.library.toolkits.DataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncCode
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncRule
import com.gogoh5.apps.quanmaomao.library.toolkits.SyncRuleGroup
import okhttp3.Call
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit

class HttpModule private constructor(): BaseModule() {
    companion object {
        internal val instance: HttpModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HttpModule() }
    }

    private val client : OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(3000L, TimeUnit.MILLISECONDS)
        builder.readTimeout(3000L, TimeUnit.MILLISECONDS)
        client = builder.build()
    }

    fun enqueue(request : BaseRequest<*>) : Call {
        val call = client.newCall(request.generateRequest())
        if(isHttpInit)
            call.enqueue(request)
        else enqueueCall(call, request)
        return call
    }

    fun checkInit() {
        if(!isHttpInit)
            enqueueCall(null, null)
    }

    fun reInit() {
        stopCompleted()
        isHttpInit = false
    }

    private var isRequesting = false
    private var isHttpInit: Boolean = false
    private var initSyncCode = SyncCode()
    private var callQueue = LinkedList<CallBag>()

    @Synchronized
    private fun enqueueCall(call: Call?, request: BaseRequest<*>?) {
        if(!isRequesting) {
            isRequesting = true

            val user = SysContext.getUser()
            val initRequest = InitMobileRequest(user.uid, user.sex, user.createTime)
                .dataHunter(DataHunter(this, isMainThread = false, syncRuleGroup = SyncRuleGroup(arrayOf(SyncRule(initSyncCode.nextCode(), initSyncCode)))) {
                    module, params->
                    val result = params[0] as Boolean
                    if(result)
                        SysContext.getSetting().configInitBean(params[1] as InitBean)
                    module.initCompleted(result)
                })
            client.newCall(initRequest.generateRequest()).enqueue(initRequest)
        }
        if(call != null && request != null)
            callQueue.push(CallBag(call, request))
    }

    @Synchronized
    private fun initCompleted(isSuccess: Boolean) {
        isRequesting = false
        if(isSuccess) {
            isHttpInit = true
            callQueue.forEach {
                val call = it.call
                val request = it.request
                if(!call.isCanceled && !call.isExecuted)
                    call.enqueue(request)
            }
        }
        else {
            callQueue.forEach {
                val call = it.call
                val request = it.request
                if(!call.isCanceled) {
                    call.cancel()
                    request.onFailure("全局初始化失败")
                }
            }
        }
        callQueue.clear()
    }

    @Synchronized
    private fun stopCompleted() {
        initSyncCode.nextCode()
        initCompleted(false)
    }

    private data class CallBag(
        val call: Call,
        val request: BaseRequest<*>
    )
}
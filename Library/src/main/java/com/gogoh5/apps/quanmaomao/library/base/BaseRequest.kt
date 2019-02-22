package com.gogoh5.apps.quanmaomao.library.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.gogoh5.apps.quanmaomao.library.environment.constants.Http
import com.gogoh5.apps.quanmaomao.library.extensions.*
import com.gogoh5.apps.quanmaomao.library.toolkits.CallManager
import com.gogoh5.apps.quanmaomao.library.toolkits.EncryptUtils
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import com.gogoh5.apps.quanmaomao.library.toolkits.UrlPath
import com.gogoh5.apps.quanmaomao.library.utils.logRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset

@Suppress("unused")
abstract class BaseRequest<T>: Callback {
    companion object {
        val EMPTY = object: BaseRequest<Any>("") {
            override fun buildRequest(): Request.Builder = Request.Builder()
            override fun analyzeResponse(response: Response): Any? = null
        }
    }

    val url: String
    private var dataHunter: RefDataHunter<*>? = null
    private var otherParams : Any? = null
    private var requestBuilderHook : RequestBuilderHooker? = null
    private var responseAnalyzeHooker: TakeRunResult<T, *>? = null
    private var responseAnalyzeMoreHooker: TakeRunMoreResult<T, Any>? = null
    private var successResponseHooker: SuccessResponseHooker<T>? = null
    private var failureResponseHooker: FailureResponseHooker? = null

    private var errorTxt: String? = null
    private var callCode: Int? = null
    private var callManager: CallManager? = null

    constructor(urlPath: UrlPath): this(urlPath.url)
    constructor(url: String) {
        this.url = url
    }

    fun generateRequest() : Request {
        val requestBuilder = buildRequest()
        requestBuilderHook?.let { it(requestBuilder) }
        val request = requestBuilder.build()
        logRequest(request.url())
        return request
    }


    override fun onResponse(call: Call, response: Response) {
        try {
            val respData : T? = analyzeResponse(response)
            successResponseHooker?.let {
                onResult(it(respData, otherParams))
                return
            }

            val finalData = responseAnalyzeMoreHooker?.let { it(respData, otherParams) } ?:
                responseAnalyzeHooker?.let { it(respData) } ?: analyzeResult(respData)

            if (finalData != null) {
                if(finalData is ErrorClass)
                    onFailure(finalData)
                else onSuccess(finalData)
            }
            else onFailure(errorTxt?:"服务器繁忙，请稍后")
        }
        catch (e: Exception) {
            onFailure("服务器解析错误，请重试")
        }
        finally {
            try {
                response.close()
            }
            catch (e: Exception) {
            }
        }

        val callCode = this.callCode?:return
        callManager?.removeCall(callCode)
    }

    override fun onFailure(call: Call, e: IOException) {
        onFailure("网络连接失败，请重试")
        val callCode = this.callCode?:return
        callManager?.removeCall(callCode)
    }

    open fun onResult(array: Array<out Any>) {
        dataHunter?.eat(*array)
    }

    open fun onSuccess(any: Any? = null) {
        dataHunter?.eat(true, any?:Unit, otherParams?:Unit)
    }

    open fun onFailure(errorData: Any?) {
        try {
            failureResponseHooker?.let {
                onResult(it(errorData))
                return
            }
            dataHunter?.eat(false, errorData?:Unit, otherParams?:Unit)
        }
        catch (e: Exception) {

        }
    }

    /**
     * Generate request for httpclient
     */
    abstract fun buildRequest() : Request.Builder

    /**
     * Analyze response data
     */
    abstract fun analyzeResponse(response: Response) : T?

    /**
     * Analyze result data
     */
    open fun analyzeResult(result: T?): Any? = result

    /*Quick method*/
    protected fun getStringFromResponse(response: Response, isEncode: Boolean = false) : String =
        if(isEncode)
            String(EncryptUtils.decode(response.body()!!.bytes()), Charset.forName("utf-8"))
        else response.body()!!.string()

    protected fun getJSONObjectFromResponse(response: Response, isEncode: Boolean = false) : JSONObject = JSON.parseObject(getStringFromResponse(response, isEncode))

    protected fun getJSONArrayFromResponse(response: Response, isEncode: Boolean = false) : JSONArray = JSON.parseArray(getStringFromResponse(response, isEncode))

    protected fun configErrorTxt(errorTxt: String?) {
        this.errorTxt = errorTxt
    }

    /*Build method*/
    fun otherParams(otherParams: Any): BaseRequest<T> {
        this.otherParams = otherParams
        return this
    }

    fun dataHunter(dataHunter: RefDataHunter<*>): BaseRequest<T> {
        this.dataHunter = dataHunter
        return this
    }

    fun requestBuilderHook(requestBuilderHook: RequestBuilderHooker): BaseRequest<T> {
        this.requestBuilderHook = requestBuilderHook
        return this
    }

    fun responseAnalyzeHooker(responseAnalyzeHooker: TakeRunResult<T, *>): BaseRequest<T> {
        this.responseAnalyzeHooker = responseAnalyzeHooker
        return this
    }

    fun responseAnalyzeMoreHooker(responseAnalyzeMoreHooker: TakeRunMoreResult<T, Any>): BaseRequest<T> {
        this.responseAnalyzeMoreHooker = responseAnalyzeMoreHooker
        return this
    }

    fun successResponseHooker(successResponseHooker: SuccessResponseHooker<T>): BaseRequest<T> {
        this.successResponseHooker = successResponseHooker
        return this
    }

    fun failureResponseHooker(failureResponseHooker: FailureResponseHooker): BaseRequest<T> {
        this.failureResponseHooker = failureResponseHooker
        return this
    }

    fun callManager(callCode: Int, callManager: CallManager?): BaseRequest<T> {
        this.callCode = callCode
        this.callManager = callManager
        return this
    }

    interface ErrorClass
}
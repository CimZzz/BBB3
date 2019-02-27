package com.gogoh5.apps.quanmaomao.library.base

import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.toolkits.CallManager
import java.lang.ref.WeakReference

abstract class BaseMethod<T: Any>(presenter: T) {
    protected val presenterRef = WeakReference(presenter)
    protected val callManager = CallManager()


    internal fun destroy() {
        onDestroy()
        callManager.stopAll()
    }

    protected fun checkCallExist(position: Int) = callManager.checkCallExist(position)

    protected fun appendRequest(position: Int, request: BaseRequest<*>, isOnly: Boolean = true) {
        if(isOnly) {
            if(callManager.checkCallExist(position))
                return
        }
        else callManager.stopCall(position)

        request.callManager(position, callManager)

        callManager.addCall(position, SysContext.getHttp().enqueue(request))
    }


    protected fun stopCall(position: Int) {
        callManager.stopCall(position)
    }

    protected fun removeCall(position: Int) {
        callManager.removeCall(position)
    }


    protected open fun onDestroy() {

    }



}
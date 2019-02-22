package com.gogoh5.apps.quanmaomao.library.toolkits

import android.os.Looper
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.DataHunterRun
import java.lang.ref.WeakReference

open class RefDataHunter<T> (
    val objRef: WeakReference<T>,
    val isMainThread: Boolean = true,
    val syncRuleGroup: SyncRuleGroup? = null,
    val dataHunterRun: DataHunterRun<T>
) {
    private var valid : Boolean = true
    private var data : Array<out Any>? = null


    fun eat(vararg any: Any) {
        data = any
        doEat()
    }

    fun doEat() {
        if(!valid)
            return
        val obj = objRef.get() ?: return
        val data = this.data?: return
        if(syncRuleGroup?.checkNotSync() == true)
            return

        if(isMainThread) {
            if(Looper.getMainLooper() == Looper.myLooper())
                dataHunterRun(obj, data)
            else SysContext.getThread().sendDataHunter(this)
        }
        else dataHunterRun(obj, data)

    }

    fun drop() {
        valid = false
    }
}
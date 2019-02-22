package com.gogoh5.apps.quanmaomao.library.environment.modules

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.gogoh5.apps.quanmaomao.library.extensions.GetResultNotNull
import com.gogoh5.apps.quanmaomao.library.extensions.Run
import com.gogoh5.apps.quanmaomao.library.toolkits.RefDataHunter
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ThreadModule internal constructor() {
    companion object {
        private const val WHAT_DATA_HUNTER = -1

        internal val instance : ThreadModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ThreadModule() }
    }

    private val handler : Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            if(msg == null)
                return

            when (msg.what) {
                WHAT_DATA_HUNTER -> {
                    val dataHunter = msg.obj as RefDataHunter<*>
                    dataHunter.doEat()
                }
            }
        }
    }

    private val threadPool: Executor = Executors.newCachedThreadPool()

    fun sendDataHunter(dataHunter: RefDataHunter<*>, delay : Long = -1) {
        val msg = handler.obtainMessage(WHAT_DATA_HUNTER, dataHunter)
        if(delay == -1L) {
            msg.sendToTarget()
        }
        else handler.sendMessageDelayed(msg, delay)
    }

    fun sendRunnableWithHunter(dataHunter: RefDataHunter<*>, runnable: GetResultNotNull<out Any>) {
        threadPool.execute {
            dataHunter.eat(runnable())
        }
    }

    fun post(delay: Long = 0L, run: Run) {
        if(delay == 0L)
            handler.post(run)
        else handler.postDelayed(run, delay)
    }
}


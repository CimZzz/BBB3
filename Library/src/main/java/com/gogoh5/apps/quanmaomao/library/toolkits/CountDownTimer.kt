package com.gogoh5.apps.quanmaomao.library.toolkits

import android.os.CountDownTimer

class CountDownTimer: CountDownTimer{
    private var callback: ((Boolean, Long) -> Unit)? = null
    private var dataHunter: RefDataHunter<*>? = null
    constructor(millisInFuture: Long, countDownInterval: Long, callback: ((Boolean, Long) -> Unit)) : super(millisInFuture, countDownInterval) {
        this.callback = callback
    }

    constructor(millisInFuture: Long, countDownInterval: Long, dataHunter: RefDataHunter<*>) : super(millisInFuture, countDownInterval) {
        this.dataHunter = dataHunter
    }


    override fun onFinish() {
        callback?.let { it(true, 0L) } ?: dataHunter?.eat(true, 0L)
    }

    override fun onTick(millisUntilFinished: Long) {
        callback?.let { it(false, millisUntilFinished) } ?: dataHunter?.eat(false, millisUntilFinished)
    }
}
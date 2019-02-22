package com.gogoh5.apps.quanmaomao.library.toolkits

class SyncCode {
    var code = 0
        private set
    var recordCode = 0
        private set
    var finished = false

    @Synchronized
    fun checkCode(code : Int) = !finished && this.code == code

    @Synchronized
    fun nextCode() : Int {
        code ++
        return code
    }

    @Synchronized
    fun checkRecordCode(code: Int) = !finished && this.recordCode == code

    @Synchronized
    fun recodeCode(code : Int = this.code) {
        recordCode = code
    }

    @Synchronized
    fun finish() {
        finished = true
    }
}
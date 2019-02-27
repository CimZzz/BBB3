package com.gogoh5.apps.quanmaomao.library.toolkits

data class SyncRuleGroup(
    private val ruleGroup: Array<out SyncRule>
) {

    fun checkNotSync(): Boolean {
        return !checkSync()
    }

    fun checkSync(): Boolean {
        ruleGroup.forEach {
            if(!it.checkSync())
                return false
        }
        return true
    }
}

data class SyncRule(
    val code: Int,
    val codeObj: SyncCode,
    val ruleType: Int = TYPE_SYNC_CODE
) {
    companion object {
        const val TYPE_SYNC_CODE = 0
        const val TYPE_REOCRD_CODE = 1
    }

    fun checkSync() = when(ruleType) {
        TYPE_REOCRD_CODE -> codeObj.checkRecordCode(code)
        else -> codeObj.checkCode(code)
    }
}


fun syncGroupOf(vararg rules: SyncRule): SyncRuleGroup {
    return SyncRuleGroup(rules)
}

package com.gogoh5.apps.quanmaomao.library.toolkits

import com.gogoh5.apps.quanmaomao.library.extensions.DataHunterRun
import java.lang.ref.WeakReference

open class DataHunter<T>(
    obj: T,
    isMainThread: Boolean = true,
    syncRuleGroup: SyncRuleGroup? = null,
    dataHunterRun: DataHunterRun<T>
) : RefDataHunter<T>(WeakReference(obj), isMainThread, syncRuleGroup, dataHunterRun)
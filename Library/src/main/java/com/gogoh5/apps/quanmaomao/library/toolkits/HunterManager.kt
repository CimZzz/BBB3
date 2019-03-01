package com.gogoh5.apps.quanmaomao.library.toolkits

class HunterManager {
    private val hunterMap = HashMap<String, RefDataHunter<*>>()
    var eatSwitch: Boolean = true

    fun register(name: String, refDataHunter: RefDataHunter<*>) {
        hunterMap[name] = refDataHunter
    }

    fun unregister(name: String) {
        hunterMap.remove(name)
    }

    fun destroy() {
        eatSwitch = true
        hunterMap.values.forEach {
            it.drop()
        }
        hunterMap.clear()
    }

    fun doEat(vararg params: Any) {
        if(!eatSwitch)
            return
        hunterMap.values.forEach {
            it.eat(*params)
        }
    }

}
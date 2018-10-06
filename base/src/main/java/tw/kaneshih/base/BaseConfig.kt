package tw.kaneshih.base

import tw.kaneshih.base.log.Logcat

object BaseConfig {

    private var isInit = false

    internal lateinit var logcat: Logcat

    fun init(logcat: Logcat) {
        if (isInit) {
            return
        }
        this.logcat = logcat

        isInit = true
    }
}
package tw.kaneshih.kanetest

import android.app.Application
import tw.kaneshih.base.BaseConfig
import tw.kaneshih.base.log.Logcat

abstract class AbstractApp : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseConfig.init(createLogcat())
    }

    abstract fun createLogcat(): Logcat
}
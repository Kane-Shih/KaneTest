package tw.kaneshih.kanetest

import android.util.Log
import tw.kaneshih.base.log.Logcat

class App : AbstractApp() {
    override fun createLogcat() = object : Logcat {
        override fun logDebug(tag: String, msg: Any?) {
            Log.d(tag, "$msg")
        }

        override fun logWarning(tag: String, msg: Any?) {
            Log.w(tag, "$msg")
        }

        override fun logError(tag: String, msg: Any?, e: Throwable?) {
            Log.e(tag, "$msg", e)
        }

    }
}
package tw.kaneshih.base.log

import tw.kaneshih.base.BaseConfig
import tw.kaneshih.base.LOG_TAG
import tw.kaneshih.base.task.Result

interface Logcat {
    fun logDebug(tag: String, msg: Any?)
    fun logWarning(tag: String, msg: Any?)
    fun logError(tag: String, msg: Any?, e: Throwable? = null)
}

fun Any?.logDebug(tag: String = LOG_TAG) = BaseConfig.logcat.logDebug(tag, "$this")

fun Any?.logWarning(tag: String = LOG_TAG) = BaseConfig.logcat.logWarning(tag, "$this")

fun Any?.logError(tag: String = LOG_TAG) {
    if (this is Throwable) {
        this.logError(tag)
    } else {
        BaseConfig.logcat.logError(tag, "$this")
    }
}

fun Throwable?.logError(tag: String = LOG_TAG, msg: String? = null) {
    BaseConfig.logcat.logError(tag, msg ?: "ERROR", this)
}

fun Result<*>?.logcat(tag: String = LOG_TAG) {
    when {
        this == null ->
            "Result is null".logError(tag)
        isSuccess ->
            "$taskName's Result SUCCESS: $data".logDebug(tag)
        else ->
            exception.logError(
                    tag = tag,
                    msg = "$taskName's Result FAILED: $errorMsg")
    }
}
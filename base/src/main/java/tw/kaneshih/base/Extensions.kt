package tw.kaneshih.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.annotation.DrawableRes
import android.util.Log
import android.util.TypedValue
import android.view.View
import tw.kaneshih.base.task.Result

const val LOG_TAG = "Kane"

fun Any?.logDebug(tag: String = LOG_TAG) = Log.d(tag, "$this")

fun Any?.logError(tag: String = LOG_TAG) {
    if (this is Throwable) {
        this.logError(tag)
    } else {
        Log.e(tag, "$this")
    }
}

fun Throwable?.logError(tag: String = LOG_TAG, msg: String? = null) {
    Log.e(tag, msg ?: "ERROR", this)
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

@DrawableRes
fun Context.getSelectableItemBackground() =
        with(TypedValue()) {
            this@getSelectableItemBackground.theme
                    .resolveAttribute(R.attr.selectableItemBackground, this, true)
            this.resourceId
        }

fun View.setSelectableItemBackground() =
        setBackgroundResource(context.getSelectableItemBackground())

fun Context.isNetworkConnected() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo
                ?.isConnected == true
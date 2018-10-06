package tw.kaneshih.base

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.DrawableRes
import android.util.TypedValue
import android.view.View
import tw.kaneshih.base.task.Result

const val LOG_TAG = "Kane"

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
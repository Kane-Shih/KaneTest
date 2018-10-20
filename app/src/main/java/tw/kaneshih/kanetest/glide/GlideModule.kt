package tw.kaneshih.kanetest.glide

import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideModule : AppGlideModule() {}

fun ImageView.load(url: String) {
    GlideApp.with(context)
            .load(url)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_rotate)
            .dontAnimate()
            .into(this)
}
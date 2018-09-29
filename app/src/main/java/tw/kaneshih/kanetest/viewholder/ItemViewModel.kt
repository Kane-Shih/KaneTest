package tw.kaneshih.kanetest.viewholder

import tw.kaneshih.base.viewholder.ViewModel

open class ItemViewModel(
        val title: String,
        val imageUrl: String,
        val count: String,
        val url: String)
    : ViewModel

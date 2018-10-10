package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.glide.GlideApp
import tw.kaneshih.kanetest.model.Book

class LargeItemVH(itemView: View,
                  itemClickListener: (viewHolder: BasicVH<*>, basicVM: BasicVM) -> Unit,
                  thumbnailClickListener: (viewHolder: BasicVH<*>, basicVM: BasicVM) -> Unit
) : BasicVH<LargeItemVM>(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val count: TextView = itemView.findViewById(R.id.count)

    init {
        itemView.setOnClickListener { itemClickListener(this, viewModel) }
        image.setOnClickListener { thumbnailClickListener(this, viewModel) }
    }

    private lateinit var viewModel: LargeItemVM

    override fun bind(viewModel: LargeItemVM) {
        this.viewModel = viewModel

        GlideApp.with(image.context)
                .load(viewModel.imageUrl)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_rotate)
                .dontAnimate()
                .into(image)

        title.text = viewModel.title
        count.text = viewModel.count
    }
}

class LargeItemVM(
        val title: String,
        val imageUrl: String,
        val count: String,
        val url: String)
    : BasicVM()

fun Card.toLargeItemViewModel(): LargeItemVM {
    return LargeItemVM(
            title = this.name,
            imageUrl = this.thumbnail,
            count = "${this.subItemCount}",
            url = this.url).apply {
        userData = this@toLargeItemViewModel
    }
}

fun Book.toLargeItemViewModel(ranking: Int): LargeItemVM {
    return LargeItemVM(
            title = this.title,
            imageUrl = this.thumbnail,
            count = "$ranking",
            url = this.url
    ).apply {
        userData = this@toLargeItemViewModel
    }
}
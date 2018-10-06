package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.kaneshih.base.viewholder.ItemViewModel
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.glide.GlideApp
import tw.kaneshih.kanetest.model.Book

class LargeItemVH(private val itemView: View,
                  itemClickListener: (itemViewModel: ItemViewModel) -> Unit,
                  thumbnailClickListener: (itemViewModel: ItemViewModel) -> Unit
) : ViewHolder<LargeItemViewModel> {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val count: TextView = itemView.findViewById(R.id.count)

    init {
        itemView.setOnClickListener { itemClickListener(viewModel) }
        image.setOnClickListener { thumbnailClickListener(viewModel) }
    }

    private lateinit var viewModel: LargeItemViewModel

    override fun bind(viewModel: LargeItemViewModel) {
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

    override fun getView() = itemView
}

class LargeItemViewModel(
        val title: String,
        val imageUrl: String,
        val count: String,
        val url: String)
    : ItemViewModel()

fun Card.toLargeItemViewModel(): LargeItemViewModel {
    return LargeItemViewModel(
            title = this.name,
            imageUrl = this.thumbnail,
            count = "${this.subItemCount}",
            url = this.url).apply {
        data = this@toLargeItemViewModel
    }
}

fun Book.toLargeItemViewModel(ranking: Int): LargeItemViewModel {
    return LargeItemViewModel(
            title = this.title,
            imageUrl = this.thumbnail,
            count = "$ranking",
            url = this.url
    ).apply {
        data = this@toLargeItemViewModel
    }
}
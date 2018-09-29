package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.card.model.Card
import tw.kaneshih.kanetest.glide.GlideApp

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
        title: String,
        imageUrl: String,
        count: String,
        url: String)
    : ItemViewModel(title, imageUrl, count, url)

fun Card.toLargeItemViewModel(): LargeItemViewModel {
    return LargeItemViewModel(
            title = this.name,
            imageUrl = this.thumbnail,
            count = "${this.subItemCount}",
            url = this.url)
}
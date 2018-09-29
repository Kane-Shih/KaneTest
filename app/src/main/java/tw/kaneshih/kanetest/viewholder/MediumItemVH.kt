package tw.kaneshih.kanetest.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.glide.GlideApp
import tw.kaneshih.kanetest.model.Book

class MediumItemVH(private val itemView: View,
                   itemClickListener: (itemViewModel: ItemViewModel) -> Unit,
                   thumbnailClickListener: (itemViewModel: ItemViewModel) -> Unit
) : ViewHolder<MediumItemViewModel> {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val count: TextView = itemView.findViewById(R.id.count)
    private val desc: TextView = itemView.findViewById(R.id.desc)

    init {
        itemView.setOnClickListener { itemClickListener(viewModel) }
        image.setOnClickListener { thumbnailClickListener(viewModel) }
    }

    private lateinit var viewModel: MediumItemViewModel

    override fun bind(viewModel: MediumItemViewModel) {
        this.viewModel = viewModel

        GlideApp.with(image.context)
                .load(viewModel.imageUrl)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_rotate)
                .dontAnimate()
                .into(image)

        title.text = viewModel.title
        count.text = viewModel.count
        desc.text = viewModel.desc
    }

    override fun getView() = itemView
}

class MediumItemViewModel(
        val title: String,
        val imageUrl: String,
        val count: String,
        val desc: String,
        val url: String,
        override val userData: Any?)
    : ItemViewModel()

fun Card.toMediumItemViewModel(context: Context?): MediumItemViewModel {
    return MediumItemViewModel(
            title = context?.getString(R.string.card_name_formatter, this.name)
                    ?: this.name,
            imageUrl = this.thumbnail,
            count = context?.getString(R.string.card_count_formatter, this.subItemCount)
                    ?: "${this.subItemCount}",
            desc = this.description,
            url = this.url,
            userData = this.id)
}

fun Book.toMediumItemViewModel(context: Context?): MediumItemViewModel {
    return MediumItemViewModel(
            title = context?.getString(R.string.book_title_formatter, this.title)
                    ?: this.title,
            imageUrl = this.thumbnail,
            count = "",
            desc = context?.getString(R.string.book_desc_formatter, this.authorName, this.publishYear, this.description)
                    ?: this.description,
            url = this.url,
            userData = this.id)
}
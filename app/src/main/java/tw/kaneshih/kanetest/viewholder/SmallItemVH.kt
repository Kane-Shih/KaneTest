package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.glide.load
import tw.kaneshih.kanetest.model.Book

class SmallItemVH(itemView: View,
                  itemClickListener: (viewHolder: BasicVH<*>, basicVM: BasicVM) -> Unit
) : BasicVH<SmallItemVM>(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)

    init {
        itemView.setOnClickListener { itemClickListener(this, viewModel) }
    }

    private lateinit var viewModel: SmallItemVM

    override fun bind(viewModel: SmallItemVM) {
        this.viewModel = viewModel

        image.load(viewModel.imageUrl)
        title.text = viewModel.title
    }
}

class SmallItemVM(
        val title: String,
        val imageUrl: String)
    : BasicVM()

fun Card.toSmallItemVM(): SmallItemVM {
    return SmallItemVM(
            title = this.name,
            imageUrl = this.thumbnail).apply {
        userData = this@toSmallItemVM
    }
}

fun Book.toSmallItemVM(): SmallItemVM {
    return SmallItemVM(
            title = this.title,
            imageUrl = this.thumbnail
    ).apply {
        userData = this@toSmallItemVM
    }
}
package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.TextView
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.base.viewholder.ItemViewModel
import tw.kaneshih.kanetest.R

class TextItemVH(itemView: View)
    : BasicVH<TextViewModel>(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.text)

    override fun bind(viewModel: TextViewModel) {
        textView.text = viewModel.text
    }
}

class TextViewModel(val text: String) : ItemViewModel()
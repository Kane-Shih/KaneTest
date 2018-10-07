package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.TextView
import tw.kaneshih.base.viewholder.ItemViewModel
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.kanetest.R

class TextItemVH(private val itemView: View)
    : ViewHolder<TextViewModel> {
    private val textView: TextView = itemView.findViewById(R.id.text)

    override fun bind(viewModel: TextViewModel) {
        textView.text = viewModel.text
    }

    override fun getView() = itemView
}

class TextViewModel(val text: String) : ItemViewModel()
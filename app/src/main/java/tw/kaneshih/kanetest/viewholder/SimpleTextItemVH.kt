package tw.kaneshih.kanetest.viewholder

import android.view.View
import android.widget.TextView
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.R

class TextItemVH(itemView: View)
    : BasicVH<TextVM>(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.text)

    override fun bind(viewModel: TextVM) {
        textView.text = viewModel.text
    }
}

class TextVM(val text: String) : BasicVM()
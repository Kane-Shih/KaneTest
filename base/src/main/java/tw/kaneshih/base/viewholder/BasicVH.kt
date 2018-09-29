package tw.kaneshih.base.viewholder

import android.view.View

class BasicVH<in VM : ViewModel>(private val view: View) : ViewHolder<VM> {
    override fun getView() = view

    override fun bind(viewModel: VM) {
        // nothing to do
    }
}
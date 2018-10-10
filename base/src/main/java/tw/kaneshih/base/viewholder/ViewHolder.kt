package tw.kaneshih.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View

interface ViewHolder<in VM : ViewModel> {
    fun bind(viewModel: VM)
    fun getView(): View
}

open class BasicVH<in VM : ItemViewModel>(private val view: View)
    : ViewHolder<VM> {
    override fun getView() = view

    override fun bind(viewModel: VM) {
        // nothing to do
    }
}

class RecyclerVH<in VM : ItemViewModel>(val viewHolder: BasicVH<VM>)
    : RecyclerView.ViewHolder(viewHolder.getView())
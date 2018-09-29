package tw.kaneshih.base.viewholder

import android.view.View

interface ViewHolder<in T : ViewModel> {
    fun bind(viewModel: T)
    fun getView(): View
}

interface ViewModel {
    val userData: Any?
}
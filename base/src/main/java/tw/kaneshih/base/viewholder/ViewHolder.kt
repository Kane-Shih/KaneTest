package tw.kaneshih.base.viewholder

import android.view.View

interface ViewHolder<T : ViewModel> {
    fun bind(viewModel: T)
    fun getViewModel(): T
    fun getView(): View
}

interface ViewModel
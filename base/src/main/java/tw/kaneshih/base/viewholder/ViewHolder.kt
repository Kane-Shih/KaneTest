package tw.kaneshih.base.viewholder

import android.os.Bundle
import android.view.View

interface ViewHolder<in T : ViewModel> {
    fun bind(viewModel: T)
    fun getView(): View
}

interface ViewModel {
    val extra: Bundle
}
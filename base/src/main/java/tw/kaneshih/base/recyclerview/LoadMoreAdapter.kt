package tw.kaneshih.base.recyclerview

import android.support.v7.widget.RecyclerView

abstract class LoadMoreAdapter<VH : RecyclerView.ViewHolder, in T> : RecyclerView.Adapter<VH>() {

    var isLoading: Boolean = false
        private set

    abstract val countWithoutLoadingItem: Int

    protected fun isItemViewTypeLoading(position: Int): Boolean {
        return position >= countWithoutLoadingItem
    }

    override fun getItemCount(): Int {
        return countWithoutLoadingItem + if (isLoading) 1 else 0
    }

    fun startLoading() {
        if (!isLoading) {
            isLoading = true
            notifyItemInserted(countWithoutLoadingItem + 1)
        }
    }

    fun stopLoading() {
        if (isLoading) {
            isLoading = false
            notifyItemRemoved(countWithoutLoadingItem + 1)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun refreshData(data: List<T>) {
        stopLoading()
        onReadyToRefresh(data)
    }

    protected abstract fun onReadyToRefresh(data: List<T>)

    @Suppress("UNCHECKED_CAST")
    fun appendData(data: List<T>) {
        stopLoading()
        onReadyToAppend(data)
    }

    protected abstract fun onReadyToAppend(data: List<T>)
}

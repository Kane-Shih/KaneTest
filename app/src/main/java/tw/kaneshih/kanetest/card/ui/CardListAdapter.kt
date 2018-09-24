package tw.kaneshih.kanetest.card.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.base.viewholder.ViewModel
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.card.ui.viewholder.ItemViewModel
import tw.kaneshih.kanetest.card.ui.viewholder.LargeItemVH
import tw.kaneshih.kanetest.card.ui.viewholder.LargeItemViewModel
import tw.kaneshih.kanetest.card.ui.viewholder.MediumItemVH
import tw.kaneshih.kanetest.card.ui.viewholder.MediumItemViewModel

class CardListAdapter(
        private val onItemClickListener: (itemViewModel: ItemViewModel) -> Unit,
        private val onItemThumbnailClickListener: (itemViewModel: ItemViewModel) -> Unit
) : LoadMoreAdapter<RecyclerView.ViewHolder, ItemViewModel>() {
    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_CARD_LARGE = 1
        const val VIEW_TYPE_CARD_MEDIUM = 2
    }

    private val list = mutableListOf<ItemViewModel>()

    override val countWithoutLoadingItem: Int
        get() = list.size

    override fun onReadyToRefresh(data: List<ItemViewModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onReadyToAppend(data: List<ItemViewModel>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isItemViewTypeLoading(position)) {
            VIEW_TYPE_LOADING
        } else {
            when (list[position]) {
                is MediumItemViewModel -> VIEW_TYPE_CARD_MEDIUM
                is LargeItemViewModel -> VIEW_TYPE_CARD_LARGE
                else -> VIEW_TYPE_CARD_MEDIUM
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CARD_LARGE ->
                LargeItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_large, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            VIEW_TYPE_CARD_MEDIUM ->
                MediumItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_medium, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            else ->
                object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_load, parent, false)) {
                    // override nothing
                }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType != VIEW_TYPE_LOADING) {
            @Suppress("UNCHECKED_CAST")
            (holder as ViewHolder<ViewModel>).bind(list[position])
        }
    }
}
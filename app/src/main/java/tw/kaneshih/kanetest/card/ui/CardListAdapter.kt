package tw.kaneshih.kanetest.card.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import tw.kaneshih.base.viewholder.ViewHolder
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.viewholder.RecyclerVH
import tw.kaneshih.kanetest.R
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.kanetest.viewholder.ItemViewModel
import tw.kaneshih.kanetest.viewholder.LargeItemVH
import tw.kaneshih.kanetest.viewholder.LargeItemViewModel
import tw.kaneshih.kanetest.viewholder.MediumItemVH
import tw.kaneshih.kanetest.viewholder.MediumItemViewModel

class CardListAdapter(
        private val onItemClickListener: (itemViewModel: ItemViewModel) -> Unit,
        private val onItemThumbnailClickListener: (itemViewModel: ItemViewModel) -> Unit
) : LoadMoreAdapter<RecyclerVH<ItemViewModel>, ItemViewModel>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerVH<ItemViewModel> {
        val vh = when (viewType) {
            VIEW_TYPE_CARD_LARGE ->
                LargeItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_large, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            VIEW_TYPE_CARD_MEDIUM ->
                MediumItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_medium, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            else -> BasicVH<ItemViewModel>(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load, parent, false))
        }
        @Suppress("UNCHECKED_CAST") // just make it to pass compiler ...
        return RecyclerVH(vh as ViewHolder<ItemViewModel>)
    }

    override fun onBindViewHolder(holder: RecyclerVH<ItemViewModel>, position: Int) {
        if (holder.itemViewType != VIEW_TYPE_LOADING) {
            holder.viewHolder.bind(list[position])
        }
    }
}

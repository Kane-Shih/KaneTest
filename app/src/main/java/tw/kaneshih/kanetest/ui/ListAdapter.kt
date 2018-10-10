package tw.kaneshih.kanetest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.viewholder.RecyclerVH
import tw.kaneshih.kanetest.R
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.viewholder.LargeItemVH
import tw.kaneshih.kanetest.viewholder.LargeItemVM
import tw.kaneshih.kanetest.viewholder.MediumItemVH
import tw.kaneshih.kanetest.viewholder.MediumItemVM
import tw.kaneshih.kanetest.viewholder.TextItemVH
import tw.kaneshih.kanetest.viewholder.TextVM

class ListAdapter : LoadMoreAdapter<RecyclerVH<BasicVM>, BasicVM>() {

    lateinit var onItemClickListener: (viewHolder: BasicVH<*>, basicVM: BasicVM) -> Unit
    lateinit var onItemThumbnailClickListener: (viewHolder: BasicVH<*>, basicVM: BasicVM) -> Unit

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_LARGE_CENTERED = 1
        private const val VIEW_TYPE_MEDIUM_LEFT_IMAGE = 2
        private const val VIEW_TYPE_MEDIUM_RIGHT_IMAGE = 3
        private const val VIEW_TYPE_GROUP_TITLE = 4
    }

    private val list = mutableListOf<BasicVM>()

    override val countWithoutLoadingItem: Int
        get() = list.size

    override fun onReadyToRefresh(data: List<BasicVM>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onReadyToAppend(data: List<BasicVM>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isItemViewTypeLoading(position)) {
            VIEW_TYPE_LOADING
        } else {
            val viewModel = list[position]
            when (viewModel) {
                is MediumItemVM -> when (viewModel.userData) {
                    is Card -> VIEW_TYPE_MEDIUM_LEFT_IMAGE
                    is Book -> {
                        if (position % 2 == 0) VIEW_TYPE_MEDIUM_RIGHT_IMAGE
                        else VIEW_TYPE_MEDIUM_LEFT_IMAGE
                    }
                    else -> VIEW_TYPE_MEDIUM_LEFT_IMAGE
                }
                is LargeItemVM -> VIEW_TYPE_LARGE_CENTERED
                is TextVM -> VIEW_TYPE_GROUP_TITLE
                else -> VIEW_TYPE_MEDIUM_LEFT_IMAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerVH<BasicVM> {
        val vh = when (viewType) {
            VIEW_TYPE_LARGE_CENTERED ->
                LargeItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_large_centered, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            VIEW_TYPE_MEDIUM_LEFT_IMAGE ->
                MediumItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_medium_left_image, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            VIEW_TYPE_MEDIUM_RIGHT_IMAGE ->
                MediumItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_medium_right_image, parent, false),
                        onItemClickListener, onItemThumbnailClickListener)

            VIEW_TYPE_GROUP_TITLE ->
                TextItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_group_title, parent, false))

            else -> BasicVH<BasicVM>(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load, parent, false))
        }
        @Suppress("UNCHECKED_CAST") // just make it to pass compiler ...
        return RecyclerVH(vh as BasicVH<BasicVM>)
    }

    override fun onBindViewHolder(holder: RecyclerVH<BasicVM>, position: Int) {
        if (holder.itemViewType != VIEW_TYPE_LOADING) {
            holder.viewHolder.bind(list[position])
        }
    }
}

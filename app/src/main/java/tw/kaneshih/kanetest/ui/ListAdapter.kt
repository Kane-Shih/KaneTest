package tw.kaneshih.kanetest.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import tw.kaneshih.kanetest.viewholder.ListVM
import tw.kaneshih.kanetest.viewholder.MediumItemVH
import tw.kaneshih.kanetest.viewholder.MediumItemVM
import tw.kaneshih.kanetest.viewholder.SmallItemVH
import tw.kaneshih.kanetest.viewholder.SmallItemVM
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
        private const val VIEW_TYPE_CAROUSEL = 5
        private const val VIEW_TYPE_SMALL = 6
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
                is ListVM<*> -> VIEW_TYPE_CAROUSEL
                is SmallItemVM -> VIEW_TYPE_SMALL
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

            VIEW_TYPE_CAROUSEL ->
                object : BasicVH<ListVM<BasicVM>>(RecyclerView(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = ListAdapter().apply {
                        onItemClickListener = this@ListAdapter.onItemClickListener
                        onItemThumbnailClickListener = this@ListAdapter.onItemThumbnailClickListener
                    }
                }) {
                    override fun bind(viewModel: ListVM<BasicVM>) {
                        (((getView() as RecyclerView).adapter) as ListAdapter).refreshData(viewModel.list)
                    }
                }

            VIEW_TYPE_SMALL ->
                SmallItemVH(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_small, parent, false),
                        onItemClickListener)

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

package tw.kaneshih.kanetest.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.layout_refresher_recyclerview.*
import tw.kaneshih.base.logcat
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.CardType
import tw.kaneshih.kanetest.task.CardListFetcher
import tw.kaneshih.kanetest.viewholder.ItemViewModel
import tw.kaneshih.kanetest.viewholder.LargeItemViewModel
import tw.kaneshih.kanetest.viewholder.MediumItemViewModel
import tw.kaneshih.kanetest.viewholder.toLargeItemViewModel
import tw.kaneshih.kanetest.viewholder.toMediumItemViewModel

class MixedListFragment : Fragment() {
    companion object {
        const val PAGE_COUNT = 20
        const val LOAD_MORE_THRESHOLD = 5

        fun getInstance() = MixedListFragment()
    }

    private var canLoadMore = true

    private lateinit var adapter: LoadMoreAdapter<*, ItemViewModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_refresher_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MixedListAdapter(itemClickListener, itemThumbnailClickListener)
                .also { this.adapter = it }
        recyclerView.addOnScrollListener(onScrollListener)
        refresher.setOnRefreshListener { refresh() }

        refresher.isRefreshing = true
        refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.removeOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!canLoadMore || adapter.isLoading || refresher.isRefreshing) {
                return
            }
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val count = adapter.countWithoutLoadingItem
            if (count - layoutManager.findLastVisibleItemPosition() <= LOAD_MORE_THRESHOLD) {
                adapter.startLoading()
                loadMore(count)
            }
        }
    }

    private val transformer = { card: Card ->
        when (card.type) {
            CardType.LARGE -> card.toLargeItemViewModel()
            CardType.MEDIUM -> card.toMediumItemViewModel(context)
        }
    }

    private val itemClickListener: (ItemViewModel) -> Unit = { item ->
        when (item) {
            is LargeItemViewModel -> item.url
            is MediumItemViewModel -> item.url
            else -> null
        }?.let {
            context?.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
        }
    }

    private val itemThumbnailClickListener: (ItemViewModel) -> Unit = { item ->
        when (item) {
            is LargeItemViewModel -> item.imageUrl
            is MediumItemViewModel -> item.imageUrl
            else -> null
        }?.let {
            context?.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
        }
    }

    private fun refresh() = CardListFetcher(0, PAGE_COUNT, transformer, this::onRefresh).execute()

    private fun onRefresh(result: Result<List<ItemViewModel>>) {
        canLoadMore = if (result.isSuccess) {
            adapter.refreshData(result.data!!)
            result.data!!.size >= PAGE_COUNT
        } else {
            context?.toast("Failed to refresh data, err: ${result.errorMsg}")
            false
        }

        result.logcat()

        refresher.isRefreshing = false
    }

    private fun loadMore(offset: Int) = CardListFetcher(offset, PAGE_COUNT, transformer, this::onLoadMore).execute()

    private fun onLoadMore(result: Result<List<ItemViewModel>>) {
        canLoadMore = if (result.isSuccess) {
            adapter.appendData(result.data!!)
            result.data!!.size >= PAGE_COUNT
        } else {
            adapter.stopLoading()
            context?.toast("Failed to append data, err: ${result.errorMsg}")
            false
        }

        result.logcat()
    }
}
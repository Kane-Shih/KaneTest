package tw.kaneshih.kanetest.card.ui

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
import kotlinx.android.synthetic.main.fragment_card_list.*
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.card.model.Card
import tw.kaneshih.kanetest.card.model.CardType
import tw.kaneshih.kanetest.card.task.CardListFetcher
import tw.kaneshih.kanetest.viewholder.ItemViewModel
import tw.kaneshih.kanetest.viewholder.toLargeItemViewModel
import tw.kaneshih.kanetest.viewholder.toMediumItemViewModel

class CardListFragment : Fragment() {
    companion object {
        const val PAGE_COUNT = 20
        const val LOAD_MORE_THRESHOLD = 5

        fun getInstance() = CardListFragment()
    }

    private var canLoadMore = true

    private lateinit var adapter: LoadMoreAdapter<*, ItemViewModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardListView.layoutManager = LinearLayoutManager(context)
        cardListView.adapter = CardListAdapter(this::onCardItemClicked, this::onCardItemThumbnailClicked)
                .also { this.adapter = it }
        cardListView.addOnScrollListener(onScrollListener)
        refresher.setOnRefreshListener { refresh() }

        refresher.isRefreshing = true
        refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cardListView.removeOnScrollListener(onScrollListener)
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

    private fun refresh() = CardListFetcher(0, PAGE_COUNT, transformer, this::onRefresh).execute()

    private fun onRefresh(result: Result<List<ItemViewModel>>) {
        canLoadMore = if (result.isSuccess) {
            adapter.refreshData(result.data!!)
            result.data!!.size >= PAGE_COUNT
        } else {
            context?.toast("Failed to refresh data, err: ${result.errorMsg}")
            false
        }

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
    }

    private fun onCardItemClicked(card: ItemViewModel) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, card.url.toUri()))
    }

    private fun onCardItemThumbnailClicked(card: ItemViewModel) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, card.imageUrl.toUri()))
    }
}
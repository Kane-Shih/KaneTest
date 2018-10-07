package tw.kaneshih.kanetest.ui

import android.content.Context
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
import tw.kaneshih.base.isNetworkConnected
import tw.kaneshih.base.log.logDebug
import tw.kaneshih.base.log.logcat
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.CardType
import tw.kaneshih.kanetest.task.BookListFetcher
import tw.kaneshih.kanetest.task.CardListFetcher
import tw.kaneshih.kanetest.task.Error
import tw.kaneshih.kanetest.task.resolveError
import tw.kaneshih.base.viewholder.ItemViewModel
import tw.kaneshih.kanetest.viewholder.LargeItemViewModel
import tw.kaneshih.kanetest.viewholder.MediumItemViewModel
import tw.kaneshih.kanetest.viewholder.TextViewModel
import tw.kaneshih.kanetest.viewholder.toLargeItemViewModel
import tw.kaneshih.kanetest.viewholder.toMediumItemViewModel
import java.lang.RuntimeException

class ListFragment : Fragment() {
    companion object {
        const val PAGE_COUNT = 20
        const val LOAD_MORE_THRESHOLD = 5

        const val USERDATA_KEY_INDEX = "index"

        private const val ARGS_TYPE = "args_type"

        fun getInstance(args: Bundle) = ListFragment().apply {
            arguments = args
        }

        fun createArgsForCards() = Bundle().apply {
            putString(ARGS_TYPE, ListType.CARDS.name)
        }

        fun createArgsForBooks() = Bundle().apply {
            putString(ARGS_TYPE, ListType.BOOKS.name)
        }
    }

    interface Host {
        fun onUpdateTitle(title: String)
    }

    private enum class ListType {
        BOOKS, CARDS
    }

    private lateinit var listType: ListType

    private var canLoadMore = true

    private lateinit var adapter: LoadMoreAdapter<*, ItemViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listType = ListType.valueOf(arguments?.getString(ARGS_TYPE)
                ?: throw RuntimeException("Developer Error: wrong args in ListFragment"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_refresher_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ListAdapter(itemClickListener, itemThumbnailClickListener)
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

    private val cardTransformer = { index: Int, card: Card ->
        when (card.type) {
            CardType.LARGE -> card.toLargeItemViewModel()
            CardType.MEDIUM -> card.toMediumItemViewModel(context)
        }.apply {
            extra.putInt(USERDATA_KEY_INDEX, index)
        }
    }

    private val bookTransformer = { index: Int, book: Book ->
        if (index < 4) {
            book.toLargeItemViewModel(index + 1)
        } else {
            book.toMediumItemViewModel(context)
        }.apply {
            extra.putInt(USERDATA_KEY_INDEX, index)
        }
    }

    private val bookListTransformer = { list: List<ItemViewModel> ->
        list.toMutableList().apply {
            add(4, TextViewModel(">> Medium layout below"))
        }
    }

    private val itemClickListener: (ItemViewModel) -> Unit = { item ->
        when (item) {
            is LargeItemViewModel -> item.url
            is MediumItemViewModel -> item.url
            else -> null
        }?.let {
            context?.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
            "itemClicked @row${item.extra.getInt(USERDATA_KEY_INDEX)}".logDebug()
        }
    }

    private val itemThumbnailClickListener: (ItemViewModel) -> Unit = { item ->
        val data = item.userData
        when (data) {
            is Card -> "Card[${data.id}] ${data.name} @ row${item.extra.getInt(USERDATA_KEY_INDEX)}"
            is Book -> "Book[${data.id}] ${data.title} @ row${item.extra.getInt(USERDATA_KEY_INDEX)}"
            else -> null
        }?.let {
            context?.toast("image of $it is clicked!")
        }
    }

    private fun startFetcher(offset: Int, callback: (Result<List<ItemViewModel>>) -> Unit) {
        when (listType) {
            ListType.CARDS -> CardListFetcher(offset, PAGE_COUNT,
                    cardTransformer, null,
                    callback)
            ListType.BOOKS ->
                BookListFetcher(
                        offset = if (offset > 0) offset - 1 else offset, // because we insert an item in listTransformer
                        count = PAGE_COUNT,
                        itemTransformer = bookTransformer,
                        listTransformer = if (offset == 0) bookListTransformer else null,
                        callback = callback)
        }.execute()
    }

    private fun refresh() {
        startFetcher(0, this::onRefresh)

        (activity as? Host)?.onUpdateTitle("fetching list of $listType")
    }

    private fun onRefresh(result: Result<List<ItemViewModel>>) {
        val context = if (isAdded && !isRemoving) context ?: return else return

        canLoadMore = if (result.isSuccess) {
            adapter.refreshData(result.data!!)
            result.data!!.size >= PAGE_COUNT
        } else {
            context.showError(result)
            //context?.toast("Failed to refresh data, err: ${result.errorMsg}")
            false
        }

        result.logcat()

        refresher.isRefreshing = false

        (activity as? Host)?.onUpdateTitle("refreshed list of $listType, total: ${adapter.countWithoutLoadingItem}, end? ${!canLoadMore}")
    }

    private fun loadMore(offset: Int) {
        startFetcher(offset, this::onLoadMore)

        (activity as? Host)?.onUpdateTitle("appending list of $listType, offset: $offset")
    }

    private fun onLoadMore(result: Result<List<ItemViewModel>>) {
        val context = if (isAdded && !isRemoving) context ?: return else return

        canLoadMore = if (result.isSuccess) {
            adapter.appendData(result.data!!)
            result.data!!.size >= PAGE_COUNT
        } else {
            adapter.stopLoading()
            context.showError(result)
            //context?.toast("Failed to append data, err: ${result.errorMsg}")
            false
        }

        result.logcat()

        (activity as? Host)?.onUpdateTitle("appended list of $listType, total: ${adapter.countWithoutLoadingItem}, end? ${!canLoadMore}")
    }

    private fun Context.showError(result: Result<*>) {
        if (!isNetworkConnected()) {
            toast("Please check your network.")
            return
        }

        when (result.resolveError()) {
            Error.ERROR_NETWORK_CONNECTIVITY -> toast("Please check your network.\n${result.errorMsg}")
            Error.ERROR_DATA_FORMAT -> toast("Something wrong at data side.\n${result.errorMsg}")
            Error.ERROR_OTHERS -> toast("Mystery error happened.\n${result.errorMsg}")
            Error.NO_ERROR -> return
        }
    }
}
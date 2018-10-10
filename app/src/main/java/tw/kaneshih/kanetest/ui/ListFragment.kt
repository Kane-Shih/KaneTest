package tw.kaneshih.kanetest.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.layout_refresher_recyclerview.*
import tw.kaneshih.base.isNetworkConnected
import tw.kaneshih.base.log.logcat
import tw.kaneshih.base.recyclerview.LoadMoreAdapter
import tw.kaneshih.base.task.Result
import tw.kaneshih.base.viewholder.BasicVH
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.CardType
import tw.kaneshih.kanetest.task.BookListFetcher
import tw.kaneshih.kanetest.task.CardListFetcher
import tw.kaneshih.kanetest.task.Error
import tw.kaneshih.kanetest.task.resolveError
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.viewholder.LargeItemVM
import tw.kaneshih.kanetest.viewholder.MediumItemVM
import tw.kaneshih.kanetest.viewholder.TextVM
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

    private lateinit var adapter: LoadMoreAdapter<*, BasicVM>

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
        recyclerView.adapter = ListAdapter()
                .apply {
                    onItemClickListener = { viewHolder: BasicVH<*>, item: BasicVM ->
                        val vPos = recyclerView.getChildAdapterPosition(viewHolder.getView())
                        when (item) {
                            is LargeItemVM -> item.url
                            is MediumItemVM -> item.url
                            else -> null
                        }?.let {
                            //context?.startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
                            context?.toast("itemClicked @ dataPos${item.extra.getInt(USERDATA_KEY_INDEX)} / vPos:$vPos")
                        }
                    }
                    onItemThumbnailClickListener = { viewHolder: BasicVH<*>, item: BasicVM ->
                        val vPos = recyclerView.getChildAdapterPosition(viewHolder.getView())
                        val data = item.userData
                        when (data) {
                            is Card -> "Card[${data.id}] ${data.name} @ dataPos${item.extra.getInt(USERDATA_KEY_INDEX)} / vPos:$vPos"
                            is Book -> "Book[${data.id}] ${data.title} @ dataPos${item.extra.getInt(USERDATA_KEY_INDEX)} / vPos:$vPos"
                            else -> null
                        }?.let {
                            context?.toast("image of $it is clicked!")
                        }
                    }
                }.also {
                    this.adapter = it
                }
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

    private fun transformCard(index: Int, card: Card) =
            when (card.type) {
                CardType.LARGE -> card.toLargeItemViewModel()
                CardType.MEDIUM -> card.toMediumItemViewModel(context)
            }.apply {
                extra.putInt(USERDATA_KEY_INDEX, index)
            }

    private fun transformBook(index: Int, book: Book) =
            if (index < 4) {
                book.toLargeItemViewModel(index + 1)
            } else {
                book.toMediumItemViewModel(context)
            }.apply {
                extra.putInt(USERDATA_KEY_INDEX, index)
            }

    private fun transformFirstPageBookList(list: List<BasicVM>): List<BasicVM> {
        return list.toMutableList().apply {
            add(4, TextVM(">> Medium layout below"))
        }
    }

    private fun startFetcher(offset: Int, callback: (Result<List<BasicVM>>) -> Unit) {
        when (listType) {
            ListType.CARDS ->
                CardListFetcher(
                        offset = offset,
                        count = PAGE_COUNT,
                        itemTransformer = ::transformCard,
                        listTransformer = null,
                        callback = callback)
            ListType.BOOKS ->
                BookListFetcher(
                        offset = if (offset > 0) offset - 1 else offset, // because we insert an item in listTransformer
                        count = PAGE_COUNT,
                        itemTransformer = ::transformBook,
                        listTransformer = if (offset == 0) ::transformFirstPageBookList else null,
                        callback = callback)
        }.execute()
    }

    private fun refresh() {
        startFetcher(0, this::onRefresh)

        (activity as? Host)?.onUpdateTitle("fetching list of $listType")
    }

    private fun onRefresh(result: Result<List<BasicVM>>) {
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

    private fun onLoadMore(result: Result<List<BasicVM>>) {
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
            Error.ERROR_NETWORK_CONNECTIVITY -> toast("Please check your network.\n${result.exception?.message}")
            Error.ERROR_DATA_FORMAT -> toast("Something wrong at data side.\n${result.exception?.message}")
            Error.ERROR_OTHERS -> toast("Mystery error happened.\n${result.exception?.message}")
            Error.NO_ERROR -> return
        }
    }
}
package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.toBookList
import tw.kaneshih.kanetest.repo.DataSource
import tw.kaneshih.base.viewholder.ItemViewModel

class BookListFetcher(private val offset: Int,
                      private val count: Int,
                      private val transformer: (index: Int, book: Book) -> ItemViewModel,
                      callback: (Result<List<ItemViewModel>>) -> Unit)
    : CallbackTask<List<ItemViewModel>>("BookListFetcher[$offset,$count]", callback) {

    override fun doInBackground(): Result<List<ItemViewModel>> {
        val jResult = DataSource.getBookList(offset, count)
        val bookList = jResult.getJSONArray("result").toBookList()
        return resultSuccess(
                bookList.mapIndexed { index, book -> transformer(offset + index, book) })
    }
}
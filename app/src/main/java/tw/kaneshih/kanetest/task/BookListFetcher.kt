package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.toBookList
import tw.kaneshih.kanetest.repo.DataSource
import tw.kaneshih.base.viewholder.BasicVM

class BookListFetcher(private val offset: Int,
                      private val count: Int,
                      itemTransformer: (index: Int, book: Book) -> BasicVM,
                      listTransformer: ((list: List<BasicVM>) -> List<BasicVM>)?,
                      callback: (Result<List<BasicVM>>) -> Unit)
    : TransformCallbackTask<Book>("BookListFetcher[$offset,$count]", callback,
        itemTransformer, listTransformer) {

    override fun getRawDataList(): List<Book> {
        val jResult = DataSource.getBookList(offset, count)
        return jResult.getJSONArray("result").toBookList()
    }

    override fun convertIndex(index: Int): Int {
        return offset + index
    }
}
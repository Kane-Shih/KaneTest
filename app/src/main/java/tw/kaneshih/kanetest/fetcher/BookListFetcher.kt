package tw.kaneshih.kanetest.fetcher

import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.toBookList
import tw.kaneshih.kanetest.repo.DataSource

class BookListFetcher(offset: Int, count: Int)
    : PageFetcher<List<Book>>(offset, count) {
    override fun fetch(): List<Book> {
        val jResult = DataSource.getBookList(offset, count)
        return jResult.getJSONArray("result").toBookList()
    }
}
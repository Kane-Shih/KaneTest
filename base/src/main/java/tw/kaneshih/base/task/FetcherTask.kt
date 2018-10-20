package tw.kaneshih.base.task

import tw.kaneshih.base.fetcher.Fetcher
import tw.kaneshih.base.fetcher.PageFetcher

class FetcherTask<DM, VM>(
        private val fetcher: Fetcher<List<DM>>,
        private val transformer: (index: Int, data: DM) -> VM
) : CallbackTask<List<VM>>("FetcherTask:${fetcher::class.java.simpleName}") {

    private var then: ((list: List<VM>) -> List<VM>)? = null

    fun then(then: ((list: List<VM>) -> List<VM>)?): FetcherTask<DM, VM> {
        this.then = then
        return this
    }

    override fun doInBackground(): Result<List<VM>> {
        val rawList = fetcher.fetch()
        val offset = (fetcher as? PageFetcher)?.offset ?: 0
        var resultList = rawList.mapIndexed { index, item ->
            transformer(offset + index, item)
        }
        this.then?.let {
            resultList = it(resultList)
        }
        return resultSuccess(resultList)
    }
}
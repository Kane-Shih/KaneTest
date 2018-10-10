package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.fetcher.Fetcher
import tw.kaneshih.kanetest.fetcher.PageFetcher

class FetcherTask<T>(
        name: String? = null,
        private val fetcher: Fetcher<List<T>>,
        private val mapIndexed: (index: Int, data: T) -> BasicVM,
        private val resultCreator: ((list: List<BasicVM>) -> List<BasicVM>)? = null,
        callback: (Result<List<BasicVM>>) -> Unit
) : CallbackTask<List<BasicVM>>(name, callback) {

    override fun doInBackground(): Result<List<BasicVM>> {
        val rawList = fetcher.fetch()
        val offset = (fetcher as? PageFetcher)?.offset ?: 0
        var resultList = rawList.mapIndexed { index, item ->
            mapIndexed(offset + index, item)
        }
        this.resultCreator?.let {
            resultList = it(resultList)
        }
        return resultSuccess(resultList)
    }
}
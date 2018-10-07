package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.base.viewholder.ItemViewModel

abstract class TransformCallbackTask<T>(
        taskName: String,
        callback: (Result<List<ItemViewModel>>) -> Unit,
        private val itemTransformer: (index: Int, data: T) -> ItemViewModel,
        private val listTransformer: ((list: List<ItemViewModel>) -> List<ItemViewModel>)?
) : CallbackTask<List<ItemViewModel>>(taskName, callback) {

    override fun doInBackground(): Result<List<ItemViewModel>> {
        val rawList = getRawDataList()
        var resultList = rawList.mapIndexed { index, item ->
            itemTransformer(convertIndex(index), item)
        }
        listTransformer?.let {
            resultList = it(resultList)
        }
        return resultSuccess(resultList)
    }

    abstract fun getRawDataList(): List<T>

    abstract fun convertIndex(index: Int): Int
}
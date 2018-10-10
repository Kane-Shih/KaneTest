package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.base.viewholder.BasicVM

abstract class TransformCallbackTask<T>(
        taskName: String,
        callback: (Result<List<BasicVM>>) -> Unit,
        private val itemTransformer: (index: Int, data: T) -> BasicVM,
        private val listTransformer: ((list: List<BasicVM>) -> List<BasicVM>)?
) : CallbackTask<List<BasicVM>>(taskName, callback) {

    override fun doInBackground(): Result<List<BasicVM>> {
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
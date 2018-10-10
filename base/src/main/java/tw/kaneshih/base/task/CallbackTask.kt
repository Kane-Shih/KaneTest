package tw.kaneshih.base.task

import android.os.AsyncTask

abstract class CallbackTask<T>(
        private val name: String? = null,
        private val callback: (Result<T>) -> Unit)
    : AsyncTask<Void, Int, Result<T>>() {

    protected abstract fun doInBackground(): Result<T>

    override fun doInBackground(vararg params: Void?): Result<T> {
        return try {
            doInBackground()
        } catch (e: Throwable) {
            Result(name, null, e)
        }
    }

    override fun onPostExecute(result: Result<T>) {
        callback(result)
    }

    protected fun resultSuccess(data: T) =
            Result(name, data, null)

    protected fun resultFailed(exception: Throwable?) =
            Result(name, null, exception)
}

data class Result<T>(val taskName: String?,
                     val data: T?,
                     val exception: Throwable?) {
    val isSuccess = data != null
}
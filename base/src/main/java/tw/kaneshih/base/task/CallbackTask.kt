package tw.kaneshih.base.task

import android.os.AsyncTask

abstract class CallbackTask<T>(
        private val name: String? = null)
    : AsyncTask<Void, Int, Result<T>>() {

    private var callback: ((Result<T>) -> Unit)? = null

    fun callback(callback: (Result<T>) -> Unit): CallbackTask<T> {
        this.callback = callback
        return this
    }

    protected abstract fun doInBackground(): Result<T>

    override fun doInBackground(vararg params: Void?): Result<T> {
        return try {
            doInBackground()
        } catch (e: Throwable) {
            Result(name, null, e)
        }
    }

    override fun onPostExecute(result: Result<T>) {
        callback?.invoke(result)
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
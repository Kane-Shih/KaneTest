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
            Result(name, false, e.message, e, null)
        }
    }

    override fun onPostExecute(result: Result<T>) {
        callback(result)
    }

    protected fun resultSuccess(data: T) =
            Result(name, true, null, null, data)

    protected fun resultFailed(errorMsg: String?, exception: Throwable?) =
            Result(name, false, errorMsg ?: exception?.message, exception, null)
}

data class Result<T>(val taskName: String?,
                     val isSuccess: Boolean,
                     val errorMsg: String?,
                     val exception: Throwable?,
                     val data: T?)
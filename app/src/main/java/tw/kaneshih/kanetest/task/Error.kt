package tw.kaneshih.kanetest.task

import org.json.JSONException
import tw.kaneshih.base.task.Result
import java.io.IOError
import java.io.IOException

enum class Error {
    ERROR_NETWORK_CONNECTIVITY,
    ERROR_DATA_FORMAT,
    ERROR_OTHERS,
    NO_ERROR
}

fun Result<*>.resolveError(): Error {
    if (isSuccess) {
        return Error.NO_ERROR
    }

    return when (exception) {
        is IOException -> Error.ERROR_NETWORK_CONNECTIVITY
        is IOError -> Error.ERROR_NETWORK_CONNECTIVITY
        is JSONException -> Error.ERROR_DATA_FORMAT
        else -> Error.ERROR_OTHERS
    }
}
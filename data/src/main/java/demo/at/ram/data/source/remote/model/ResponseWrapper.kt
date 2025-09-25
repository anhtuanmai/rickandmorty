package demo.at.ram.data.source.remote.model

import retrofit2.Response

data class ResponseWrapper<T> (
    val response: Response<T>?,
    val error: Throwable?,
) {
    fun isSuccessful() : Boolean {
      return (error == null) && (response != null) && (response.isSuccessful)
    }

    companion object {
        fun <T> wrapHttpResponse(response: Response<T>): ResponseWrapper<T> {
            return ResponseWrapper(
                response = response,
                error = null
            )
        }

        fun <T> wrapError(error: Throwable): ResponseWrapper<T> {
            return ResponseWrapper(
                response = null,
                error = error
            )
        }
    }
}
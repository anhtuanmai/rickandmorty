package demo.at.ram.shared.model

/**
 * @param sourceOrigin Origin of data. Default is [SourceOrigin.REMOTE]
 */
data class ResponseResult<T>(
    val isSuccessful: Boolean,
    val remoteCode: Int?,
    val data: T? = null,
    val sourceOrigin: SourceOrigin = SourceOrigin.REMOTE,
) {
    companion object {
        fun <T> success(data: T, code: Int? = null): ResponseResult<out T> {
            return ResponseResult(
                isSuccessful = true,
                remoteCode = code,
                data = data
            )
        }
    }
}

enum class SourceOrigin {
    LOCAL, REMOTE
}
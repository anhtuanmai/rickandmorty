package demo.at.ram.shared.model

data class ResponseResult<T>(
    val status: EnumResponseStatus,
    val code: Int?,
    val data: T? = null,
)

enum class EnumResponseStatus {
    SUCCESS,
    ERROR_REJECTED,
    ERROR_NO_NETWORK,
    ERROR_UNKNOWN,
}
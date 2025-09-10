package demo.at.ram.shared.model

data class Result<T>(
    val data: T?= null,
    val status: EnumResultStatus,
)

enum class EnumResultStatus {
    SUCCESS,
    ERROR_REJECTED,
    ERROR_NO_NETWORK,
    ERROR_UNKNOWN,
}
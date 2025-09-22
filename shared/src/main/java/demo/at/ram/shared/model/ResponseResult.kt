package demo.at.ram.shared.model

data class ResponseResult<T>(
    val isSuccessful: Boolean,
    val code: Int?,
    val data: T? = null,
) {
    companion object {
        fun <T> success(data: T, code: Int? = null): ResponseResult<out T> {
            return ResponseResult(
                isSuccessful = true,
                code = code,
                data = data
            )
        }
    }
}
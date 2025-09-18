package demo.at.ram.shared.model

data class ResponseResult<T>(
    val isSuccessful: Boolean,
    val code: Int?,
    val data: T? = null,
)
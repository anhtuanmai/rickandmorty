package demo.at.ram.domain.error

import androidx.annotation.StringRes
import demo.at.ram.domain.R

sealed class AppError : Exception() {
    @get:StringRes
    abstract val messageResId: Int

    sealed class NetworkError : AppError() {
        object NoConnection : NetworkError() {
            override val messageResId = R.string.error_no_connection
        }

        object Timeout : NetworkError() {
            override val messageResId = R.string.error_timeout
        }

        data class ServerError(val code: Int, val serverMessage: String) : NetworkError() {
            override val messageResId = R.string.error_server
        }

        data class HttpError(val code: Int) : NetworkError() {
            override val messageResId = when (code) {
                404 -> R.string.error_not_found
                401 -> R.string.error_unauthorized
                403 -> R.string.error_forbidden
                500 -> R.string.error_internal_server
                else -> R.string.error_http_generic
            }
        }
    }

    sealed class DatabaseError : AppError() {
        object DataNotFound : DatabaseError() {
            override val messageResId = R.string.error_data_not_found
        }

        object DatabaseCorrupted : DatabaseError() {
            override val messageResId = R.string.error_database_corrupted
        }

        data class QueryError(val query: String) : DatabaseError() {
            override val messageResId = R.string.error_database_query
        }

        object CacheExpired : DatabaseError() {
            override val messageResId = R.string.error_cache_expired
        }
    }

    sealed class BusinessError : AppError() {
        data class InvalidCharacterId(val id: Long) : BusinessError() {
            override val messageResId = R.string.error_invalid_character_id
        }

        data class InvalidPageNumber(val page: Int) : BusinessError() {
            override val messageResId = R.string.error_invalid_page
        }
    }

    sealed class GeneralError : AppError() {
        data class Unknown(val originalMessage: String) : GeneralError() {
            override val messageResId = R.string.error_unknown
        }

        object UnexpectedError : GeneralError() {
            override val messageResId = R.string.error_unexpected
        }

        data class ValidationError(val field: String, val rule: String) : GeneralError() {
            override val messageResId = R.string.error_validation
        }
    }
}

fun Throwable.toAppError() : AppError {
    return when (this) {
        is AppError -> this
        else -> AppError.GeneralError.Unknown(this.message ?: "")
    }
}

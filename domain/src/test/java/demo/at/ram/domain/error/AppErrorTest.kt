package demo.at.ram.domain.error

import demo.at.ram.domain.R
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AppErrorTest {

    // ========== NetworkError Tests ==========

    @Test
    fun `NoConnection returns correct message resource`() {
        assertEquals(R.string.error_no_connection, AppError.NetworkError.NoConnection.messageResId)
    }

    @Test
    fun `Timeout returns correct message resource`() {
        assertEquals(R.string.error_timeout, AppError.NetworkError.Timeout.messageResId)
    }

    @Test
    fun `ServerError properties and message resource`() {
        val error = AppError.NetworkError.ServerError(500, "Internal error")
        assertEquals(500, error.code)
        assertEquals("Internal error", error.serverMessage)
        assertEquals(R.string.error_server, error.messageResId)
    }

    @Test
    fun `ServerError data class equality works correctly`() {
        val error1 = AppError.NetworkError.ServerError(404, "Not found")
        val error2 = AppError.NetworkError.ServerError(404, "Not found")
        val error3 = AppError.NetworkError.ServerError(500, "Server error")

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    @Test
    fun `HttpError returns error_not_found for 404`() {
        assertEquals(R.string.error_not_found, AppError.NetworkError.HttpError(404).messageResId)
    }

    @Test
    fun `HttpError returns error_unauthorized for 401`() {
        assertEquals(R.string.error_unauthorized, AppError.NetworkError.HttpError(401).messageResId)
    }

    @Test
    fun `HttpError returns error_forbidden for 403`() {
        assertEquals(R.string.error_forbidden, AppError.NetworkError.HttpError(403).messageResId)
    }

    @Test
    fun `HttpError returns error_internal_server for 500`() {
        assertEquals(
            R.string.error_internal_server,
            AppError.NetworkError.HttpError(500).messageResId
        )
    }

    @Test
    fun `HttpError returns error_http_generic for unknown codes`() {
        assertEquals(R.string.error_http_generic, AppError.NetworkError.HttpError(999).messageResId)
        assertEquals(R.string.error_http_generic, AppError.NetworkError.HttpError(503).messageResId)
        assertEquals(R.string.error_http_generic, AppError.NetworkError.HttpError(0).messageResId)
    }

    @Test
    fun `HttpError data class equality works correctly`() {
        val error1 = AppError.NetworkError.HttpError(404)
        val error2 = AppError.NetworkError.HttpError(404)
        val error3 = AppError.NetworkError.HttpError(500)

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    // ========== DatabaseError Tests ==========

    @Test
    fun `DataNotFound returns correct message resource`() {
        assertEquals(
            R.string.error_data_not_found,
            AppError.DatabaseError.DataNotFound.messageResId
        )
    }

    @Test
    fun `DatabaseCorrupted returns correct message resource`() {
        assertEquals(
            R.string.error_database_corrupted,
            AppError.DatabaseError.DatabaseCorrupted.messageResId
        )
    }

    @Test
    fun `QueryError properties and message resource`() {
        val error = AppError.DatabaseError.QueryError("SELECT * FROM users")
        assertEquals("SELECT * FROM users", error.query)
        assertEquals(R.string.error_database_query, error.messageResId)
    }

    @Test
    fun `QueryError data class equality works correctly`() {
        val error1 = AppError.DatabaseError.QueryError("SELECT *")
        val error2 = AppError.DatabaseError.QueryError("SELECT *")
        val error3 = AppError.DatabaseError.QueryError("UPDATE users")

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    @Test
    fun `CacheExpired returns correct message resource`() {
        assertEquals(R.string.error_cache_expired, AppError.DatabaseError.CacheExpired.messageResId)
    }

    // ========== BusinessError Tests ==========

    @Test
    fun `InvalidCharacterId properties and message resource`() {
        val error = AppError.BusinessError.InvalidCharacterId(12345L)
        assertEquals(12345L, error.id)
        assertEquals(R.string.error_invalid_character_id, error.messageResId)
    }

    @Test
    fun `InvalidCharacterId data class equality works correctly`() {
        val error1 = AppError.BusinessError.InvalidCharacterId(123L)
        val error2 = AppError.BusinessError.InvalidCharacterId(123L)
        val error3 = AppError.BusinessError.InvalidCharacterId(456L)

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    @Test
    fun `InvalidPageNumber properties and message resource`() {
        val error = AppError.BusinessError.InvalidPageNumber(-1)
        assertEquals(-1, error.page)
        assertEquals(R.string.error_invalid_page, error.messageResId)
    }

    @Test
    fun `InvalidPageNumber data class equality works correctly`() {
        val error1 = AppError.BusinessError.InvalidPageNumber(0)
        val error2 = AppError.BusinessError.InvalidPageNumber(0)
        val error3 = AppError.BusinessError.InvalidPageNumber(1)

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    // ========== GeneralError Tests ==========

    @Test
    fun `Unknown properties and message resource`() {
        val error = AppError.GeneralError.Unknown("Something went wrong")
        assertEquals("Something went wrong", error.originalMessage)
        assertEquals(R.string.error_unknown, error.messageResId)
    }

    @Test
    fun `Unknown data class equality works correctly`() {
        val error1 = AppError.GeneralError.Unknown("Error A")
        val error2 = AppError.GeneralError.Unknown("Error A")
        val error3 = AppError.GeneralError.Unknown("Error B")

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    @Test
    fun `UnexpectedError returns correct message resource`() {
        assertEquals(R.string.error_unexpected, AppError.GeneralError.UnexpectedError.messageResId)
    }

    @Test
    fun `ValidationError properties and message resource`() {
        val error = AppError.GeneralError.ValidationError("email", "required")
        assertEquals("email", error.field)
        assertEquals("required", error.rule)
        assertEquals(R.string.error_validation, error.messageResId)
    }

    @Test
    fun `ValidationError data class equality works correctly`() {
        val error1 = AppError.GeneralError.ValidationError("email", "required")
        val error2 = AppError.GeneralError.ValidationError("email", "required")
        val error3 = AppError.GeneralError.ValidationError("name", "minLength")

        assertEquals(error1, error2)
        assertNotEquals(error1, error3)
    }

    // ========== toAppError Extension Function Tests ==========

    @Test
    fun `toAppError returns same AppError when Throwable is already AppError`() {
        val originalError = AppError.NetworkError.NoConnection
        val result = originalError.toAppError()

        assertSame(originalError, result)
    }

    @Test
    fun `toAppError converts generic Throwable to Unknown with message`() {
        val throwable = RuntimeException("Custom error message")
        val result = throwable.toAppError()

        assertTrue(result is AppError.GeneralError.Unknown)
        assertEquals(
            "Custom error message",
            (result as AppError.GeneralError.Unknown).originalMessage
        )
        assertEquals(R.string.error_unknown, result.messageResId)
    }

    @Test
    fun `toAppError converts Throwable with null message to Unknown with empty string`() {
        val throwable = RuntimeException()
        val result = throwable.toAppError()

        assertTrue(result is AppError.GeneralError.Unknown)
        assertEquals("", (result as AppError.GeneralError.Unknown).originalMessage)
    }

    @Test
    fun `toAppError preserves specific NetworkError types`() {
        val originalError = AppError.NetworkError.Timeout
        val result = originalError.toAppError()

        assertTrue(result is AppError.NetworkError.Timeout)
        assertSame(originalError, result)
    }

    @Test
    fun `toAppError preserves DatabaseError types`() {
        val originalError = AppError.DatabaseError.CacheExpired
        val result = originalError.toAppError()

        assertTrue(result is AppError.DatabaseError.CacheExpired)
        assertSame(originalError, result)
    }

    // ========== AppError as Exception Tests ==========

    @Test
    fun `AppError can be thrown and caught`() {
        try {
            throw AppError.NetworkError.Timeout
        } catch (e: AppError) {
            assertTrue(e is AppError.NetworkError.Timeout)
            assertEquals(R.string.error_timeout, e.messageResId)
        }
    }
}
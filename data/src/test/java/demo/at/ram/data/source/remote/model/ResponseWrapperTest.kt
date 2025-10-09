package demo.at.ram.data.source.remote.model

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class ResponseWrapperTest {

    @Test
    fun isSuccessful() {
        val successResponse = Response.success("Success")

        val wrapper1 = ResponseWrapper(successResponse, null)
        assertTrue(wrapper1.isSuccessful())

        val failResponse = Response.error<String>(400, "Bad Request".toResponseBody())

        val wrapper2 = ResponseWrapper(failResponse, null)
        assertFalse(wrapper2.isSuccessful())
    }

    @Test
    fun getResponse() {
        val wrapper = ResponseWrapper.wrapHttpResponse(Response.success("Success"))

        assertTrue(wrapper.isSuccessful())
    }

    @Test
    fun getError() {
        val wrapper = ResponseWrapper.wrapError<String>(Throwable("Error"))

        assertFalse(wrapper.isSuccessful())
    }

}
package demo.at.ram.data.source.remote.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RestInfoTest {

    @Test
    fun newInstanceTest() {
        val restInfo = RestInfo(
            count = 1,
            pages = 0,
            next = "next",
            prev = "prev",
        )

        assertNotNull(restInfo)
    }
}
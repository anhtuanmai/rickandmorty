package demo.at.ram.data

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class CheckInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("demo.at.ram.data.test", appContext.packageName)
    }
}

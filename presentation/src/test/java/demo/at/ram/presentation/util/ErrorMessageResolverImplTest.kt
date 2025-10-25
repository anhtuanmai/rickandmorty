package demo.at.ram.presentation.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import demo.at.ram.domain.error.AppError
import demo.at.ram.presentation.RobolectricExtensionSelfTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.annotation.Config
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

@ExtendWith(RobolectricExtension::class)
@Config(application = RobolectricExtensionSelfTest.MyTestApplication::class)
class ErrorMessageResolverImplTest {

    lateinit var errorMessageResolverImpl : ErrorMessageResolverImpl

    @BeforeEach
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Context>()
        errorMessageResolverImpl = ErrorMessageResolverImpl(application)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun resolveErrorMessage() {
        val error = AppError.GeneralError.UnexpectedError
        val expected = "General error"

        assertEquals(expected , errorMessageResolverImpl.resolveErrorMessage(error))
    }

}
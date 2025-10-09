package demo.at.ram.data.source.remote

import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharacterRemoteDataSourceTest {

    @MockK
    lateinit var ramService: RamService

    private lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    @BeforeEach
    fun setUp() {
        ramService = mockk(relaxed = true)
        characterRemoteDataSource = CharacterRemoteDataSource(ramService)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getAllCharacters() = runTest {
        // Then
        characterRemoteDataSource.getAllCharacters()

        // Then
        coVerify(exactly = 1) { ramService.getAllCharacters() }
    }
}
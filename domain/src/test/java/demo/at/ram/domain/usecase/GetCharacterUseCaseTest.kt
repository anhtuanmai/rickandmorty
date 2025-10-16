package demo.at.ram.domain.usecase

import app.cash.turbine.test
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCharacterUseCaseTest {

    @MockK
    private lateinit var characterRepository : CharacterRepository

    private lateinit var getCharacterUseCase: GetCharacterUseCase

    @BeforeEach
    fun setUp() {
        characterRepository = mockk(relaxed = true)
        getCharacterUseCase = GetCharacterUseCase(characterRepository)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() = runTest {
        // Given
        val character = Character(id = 1, name = "Rick Sanchez")
        every { characterRepository.getCharacter(1) } returns flowOf(character)

        // When & Then
        getCharacterUseCase(1).test {
            val item = awaitItem()
            assertEquals(character, item)
            awaitComplete()
        }
    }

}
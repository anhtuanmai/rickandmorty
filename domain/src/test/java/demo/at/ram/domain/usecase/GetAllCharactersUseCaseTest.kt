package demo.at.ram.domain.usecase

import app.cash.turbine.test
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.ResponseResult
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllCharactersUseCaseTest {

    @MockK
    private lateinit var characterRepository: CharacterRepository

    private lateinit var getAllCharactersUseCase: GetAllCharactersUseCase

    @BeforeEach
    fun setup() {
        characterRepository = mockk<CharacterRepository>()
        getAllCharactersUseCase = GetAllCharactersUseCase(characterRepository)
    }

    @Test
    fun `should return characters when repository succeeds`() = runTest {
        // Given
        val allCharactersFlow = flowOf(
            ResponseResult<List<Character>>(
                isSuccessful = true,
                httpCode = 200,
                data = listOf(
                    Character(id = 1, name = "Rick Sanchez"),
                    Character(id = 2, name = "Morty Smith")
                ),
            )
        )
        every { characterRepository.getAllCharacters() } returns allCharactersFlow

        // When & Then
        getAllCharactersUseCase().test {
            val result = awaitItem()
            assertEquals(true, result.isSuccessful)
            assertEquals(2, result.data?.size)
            awaitComplete()
        }
    }
}
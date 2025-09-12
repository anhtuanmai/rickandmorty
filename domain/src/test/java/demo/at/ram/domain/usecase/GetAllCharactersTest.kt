package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.EnumResponseStatus
import demo.at.ram.shared.model.ResponseResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllCharactersTest {

    @MockK
    private lateinit var characterRepository: CharacterRepository

    private lateinit var getAllCharactersUseCase: GetAllCharacters

    @BeforeEach
    fun setup() {
        characterRepository = mockk<CharacterRepository>()
        getAllCharactersUseCase = GetAllCharacters(characterRepository)
    }

    @Test
    fun `should return characters when repository succeeds`() = runTest {
        //Given
        val expectedResult = ResponseResult<List<Character>>(
            status = EnumResponseStatus.SUCCESS,
            code = 200,
            data = listOf(
                Character(id = 1, name = "Rick Sanchez"),
                Character(id = 2, name = "Morty Smith")
            ),
        )
        coEvery { characterRepository.getAllCharacters() } returns expectedResult

        //When
        val result = getAllCharactersUseCase()

        //Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { characterRepository.getAllCharacters() }
    }
}
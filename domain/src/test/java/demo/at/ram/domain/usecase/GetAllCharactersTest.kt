package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.CharacterEntity
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.EnumResultStatus
import demo.at.ram.shared.model.Result
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
        val expectedResult = Result<List<CharacterEntity>>(
            data = listOf(
                CharacterEntity(id = 1, name = "Rick Sanchez"),
                CharacterEntity(id = 2, name = "Morty Smith")
            ),
            status = EnumResultStatus.SUCCESS
        )
        coEvery { characterRepository.getAllCharacters() } returns expectedResult

        //When
        val result = getAllCharactersUseCase()

        //Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { characterRepository.getAllCharacters() }
    }
}
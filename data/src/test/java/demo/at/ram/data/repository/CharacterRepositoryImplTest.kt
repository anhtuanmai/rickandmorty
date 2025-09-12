package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.dao.CharacterDao
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.data.source.remote.RamService
import demo.at.ram.data.source.remote.model.RestBody
import demo.at.ram.domain.model.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class CharacterRepositoryImplTest {

    @MockK
    private lateinit var ramService: RamService

    @MockK
    private lateinit var characterDao: CharacterDao

    private lateinit var repository: CharacterRepositoryImpl

    @BeforeEach
    fun setUp() {
        ramService = mockk<RamService>()
        characterDao = mockk<CharacterDao>()
        repository = CharacterRepositoryImpl(
            remoteDataSource = CharacterRemoteDataSource(ramService),
            localDataSource = CharacterLocalDataSource(characterDao)
        )
    }

    @Test
    fun getAllCharacters() = runTest {
        //Given
        val expectedCharacters = listOf(
            Character(id = 1, name = "Rick Sanchez"),
            Character(id = 2, name = "Morty Smith")
        )
        val mockResult = Response.success(RestBody(info = null, results = expectedCharacters))
        coEvery { ramService.getAllCharacters() } returns mockResult

        //When
        val characters = repository.getAllCharacters().data

        //Then
        assertEquals(expectedCharacters, characters)
        coVerify(exactly = 1) { ramService.getAllCharacters() }
    }

    @Test
    fun getSavedCharacters() = runTest {
        //Given
        val expectedCharacters = listOf(
            Character(id = 1, name = "Rick Sanchez"),
            Character(id = 2, name = "Morty Smith")
        )
        coEvery { characterDao.getAll() } returns expectedCharacters.map { CharacterEntity(it) }

        //When
        val characters = repository.getSavedCharacters()

        //Then
        assertEquals(expectedCharacters, characters)
        coVerify(exactly = 1) { characterDao.getAll() }
    }

}
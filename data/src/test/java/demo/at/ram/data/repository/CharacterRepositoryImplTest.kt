package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.data.source.remote.model.ResponseWrapper
import demo.at.ram.data.source.remote.model.RestBody
import demo.at.ram.domain.model.Character
import demo.at.ram.shared.model.SourceOrigin
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.io.IOException

class CharacterRepositoryImplTest {

    @MockK
    private lateinit var remoteDataSource: CharacterRemoteDataSource

    @MockK
    private lateinit var localDataSource: CharacterLocalDataSource

    private lateinit var testScope: CoroutineScope
    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var repository: CharacterRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteDataSource = mockk<CharacterRemoteDataSource>(relaxed = true)
        localDataSource = mockk<CharacterLocalDataSource>(relaxed = true)

        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        testScope = CoroutineScope(SupervisorJob() + testScheduler)
        repository = CharacterRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            applicationScope = testScope,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun getAllCharacters_testSuccessRemote() = runTest(testScheduler) {
        // Given : Remote OK
        val expectedCharacters = listOf(
            Character(id = 1, name = "Rick Sanchez"),
            Character(id = 2, name = "Morty Smith")
        )
        expectedCharacters.map { CharacterEntity(it) }
        expectedCharacters.map { it.id }
        val mockResult = Response.success(RestBody(info = null, results = expectedCharacters))
        coEvery { remoteDataSource.getAllCharacters() } returns ResponseWrapper.wrapHttpResponse(
            mockResult
        )

        // When
        val response = repository.getAllCharacters().first()

        // Then
        assertEquals(true, response.isSuccessful)
        assertEquals(SourceOrigin.REMOTE, response.sourceOrigin)
        assertEquals(expectedCharacters, response.data)
        coVerify(exactly = 1) { remoteDataSource.getAllCharacters() }
        coVerify(exactly = 0) { localDataSource.loadCharacters() }
        coVerify(exactly = 1) { localDataSource.saveCharacters(any()) }
    }

    @Test
    fun getAllCharacters_testSuccessLocal() = runTest {
        // Given : Remote 404 + not empty db
        coEvery { remoteDataSource.getAllCharacters() } returns
                ResponseWrapper.wrapError(IOException("No internet connection"))

        val expectedCharacters = listOf(
            Character(id = 1, name = "Rick Sanchez"),
            Character(id = 2, name = "Morty Smith")
        )
        coEvery { localDataSource.loadCharacters() } returns expectedCharacters.map {
            CharacterEntity(
                it
            )
        }

        //When
        val response = repository.getAllCharacters().first()
        val characters = response.data

        // Then
        assertEquals(true, response.isSuccessful)
        assertEquals(SourceOrigin.LOCAL, response.sourceOrigin)
        assertEquals(expectedCharacters, characters)
        coVerify(exactly = 1) { remoteDataSource.getAllCharacters() }
        coVerify(exactly = 1) { localDataSource.loadCharacters() }
        coVerify(exactly = 0) { localDataSource.saveCharacters(any()) }
    }

    @Test
    fun getAllCharacters_testFailure() = runTest {
        // Given : Remote 404 + empty db
        coEvery { remoteDataSource.getAllCharacters() } returns
                ResponseWrapper.wrapError(IOException("No internet connection"))
        coEvery { localDataSource.loadCharacters() } returns emptyList()

        //When
        val response = repository.getAllCharacters().first()

        //Then
        assertEquals(false, response.isSuccessful)
        coVerify(exactly = 1) { remoteDataSource.getAllCharacters() }
        coVerify(exactly = 1) { localDataSource.loadCharacters() }
        coVerify(exactly = 0) { localDataSource.saveCharacters(any()) }
    }

    @Test
    fun getSavedCharacters() = runTest {
        // Given
        val expectedCharacters = listOf(
            Character(id = 1, name = "Rick Sanchez"),
            Character(id = 2, name = "Morty Smith")
        )
        coEvery { localDataSource.loadCharacters() } returns expectedCharacters.map {
            CharacterEntity(
                it
            )
        }

        //When
        val characters = repository.getSavedCharacters().first()

        //Then
        assertEquals(expectedCharacters, characters)
        coVerify(exactly = 1) { localDataSource.loadCharacters() }
    }

}
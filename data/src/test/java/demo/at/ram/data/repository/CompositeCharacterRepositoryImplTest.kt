package demo.at.ram.data.repository

import app.cash.turbine.test
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.UserDataRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CompositeCharacterRepositoryImplTest {

    @MockK
    lateinit var characterRepository: CharacterRepository

    @MockK
    lateinit var userDataRepository: UserDataRepository

    lateinit var compositeImpl: CompositeCharacterRepositoryImpl

    @BeforeEach
    fun setUp() {
        characterRepository = mockk<CharacterRepository>(relaxed = true)
        userDataRepository = mockk<UserDataRepository>(relaxed = true)

        compositeImpl = CompositeCharacterRepositoryImpl(
            characterRepository = characterRepository,
            userDataRepository = userDataRepository,
        )
    }


    @Test
    fun getFavorites() = runTest {
        // Given
        val favoritesFlow = flowOf(
            listOf<Long>(1, 4)
        )
        val allCharactersFlow = flowOf(
            listOf(
                Character(1, "Batman"),
                Character(2, "Superman"),
                Character(3, "Wonder Woman"),
                Character(4, "Flash")
            )
        )
        coEvery { userDataRepository.getFavorites() } returns favoritesFlow
        coEvery { characterRepository.getSavedCharacters() } returns allCharactersFlow

        // When & Then
        compositeImpl.getFavorites().test {
            val firstItem = awaitItem()
            assertEquals(2, firstItem.size)
            assertEquals("Batman", firstItem[0].name)
            assertEquals("Flash", firstItem[1].name)
            awaitComplete()
        }
    }

}
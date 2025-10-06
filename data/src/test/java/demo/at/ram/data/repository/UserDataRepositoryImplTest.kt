package demo.at.ram.data.repository

import app.cash.turbine.test
import demo.at.ram.data.source.local.UserPreferencesDataSource
import demo.at.ram.domain.model.DarkTheme
import demo.at.ram.domain.model.UserData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserDataRepositoryImplTest {

    @MockK
    lateinit var userPreferencesDataSource: UserPreferencesDataSource

    lateinit var userDataRepositoryImpl: UserDataRepositoryImpl

    @BeforeEach
    fun setUp() {
        userPreferencesDataSource = mockk(relaxed = true)

        userDataRepositoryImpl = UserDataRepositoryImpl(userPreferencesDataSource)
    }

    @Test
    fun getFavorites() = runTest {
        val expectedIds = listOf<Long>(1, 2, 4)
        every { userPreferencesDataSource.userData } returns flowOf(
            UserData(
                DarkTheme.DARK,
                expectedIds
            )
        )

        // When & Then
        userDataRepositoryImpl.getFavorites().test {
            val firstItem = awaitItem()
            assertEquals(expectedIds.size, firstItem.size)
            assertEquals(expectedIds[0], firstItem[0])
            awaitComplete()
        }
    }

    @Test
    fun addFavorite() = runTest {
        // When
        userDataRepositoryImpl.addFavorite(1)

        // Then
        coVerify(exactly = 1) { userPreferencesDataSource.setFavorite(any()) }
    }

    @Test
    fun removeFavorite() = runTest {
        // When
        userDataRepositoryImpl.removeFavorite(1)

        // Then
        coVerify(exactly = 1) { userPreferencesDataSource.unsetFavorite(any()) }
    }

}
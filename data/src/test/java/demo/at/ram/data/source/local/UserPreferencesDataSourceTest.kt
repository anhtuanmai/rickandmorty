package demo.at.ram.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import demo.at.ram.data.source.local.datastore.UserPreferences
import demo.at.ram.data.source.local.datastore.UserPreferencesSerializer
import demo.at.ram.domain.model.DarkTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.File

class UserPreferencesDataSourceTest {

    @TempDir(cleanup = CleanupMode.ALWAYS)
    private lateinit var tempDir: File

    private val storageName = "test_storage.protobuf"

    private lateinit var dataStore: DataStore<UserPreferences>

    private lateinit var userPreferences: UserPreferencesDataSource

    private lateinit var testScope: TestScope

    private lateinit var testDispatcher: TestDispatcher

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)

        dataStore = DataStoreFactory.create(
            serializer = UserPreferencesSerializer(),
            produceFile = {
                File(tempDir, storageName)
            },
        )

        userPreferences = UserPreferencesDataSource(dataStore)
    }

    @AfterEach
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    @DisplayName("Get user data with LIGHT theme")
    fun `Get user data with LIGHT`() = testScope.runTest {
        userPreferences.setDarkTheme(DarkTheme.LIGHT)
        val first = userPreferences.userData.first()
        assertEquals(DarkTheme.LIGHT, first.darkTheme)
    }

    @Test
    @DisplayName("Get user data with DARK theme")
    fun getDarkTheme() = testScope.runTest {
        userPreferences.setDarkTheme(DarkTheme.DARK)
        val first = userPreferences.userData.first()
        assertEquals(DarkTheme.DARK, first.darkTheme)
    }

    @Test
    @DisplayName("Modify user data on theme")
    fun `Modify user data`() = testScope.runTest {
        userPreferences.setDarkTheme(DarkTheme.LIGHT)
        val first = userPreferences.userData.first()
        assertEquals(DarkTheme.LIGHT, first.darkTheme)

        userPreferences.setDarkTheme(DarkTheme.DARK)
        val second = userPreferences.userData.first()
        assertEquals(DarkTheme.DARK, second.darkTheme)
    }

    @Test
    @DisplayName("Set/Unset favorites")
    fun setUnsetFavorite() = testScope.runTest {
        // Set favorite 1
        userPreferences.setFavorite(1L)
        val firstState = userPreferences.userData.first()
        assertEquals(1, firstState.favoriteCharacterIds.size)
        assertTrue(firstState.favoriteCharacterIds.contains(1L))

        // Set favorite 2
        userPreferences.setFavorite(2L)
        val secondState = userPreferences.userData.first()
        assertEquals(2, secondState.favoriteCharacterIds.size)
        assertTrue(secondState.favoriteCharacterIds.containsAll(listOf(1L, 2L)))

        // Unset favorite 1
        userPreferences.unsetFavorite(2L)
        val thirdState = userPreferences.userData.first()
        assertEquals(1, thirdState.favoriteCharacterIds.size)
        assertFalse(thirdState.favoriteCharacterIds.contains(2L))
    }
}
package demo.at.ram.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import demo.at.ram.data.source.local.datastore.UserPreferences
import demo.at.ram.data.source.local.datastore.UserPreferencesSerializer
import demo.at.ram.domain.model.DarkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.robolectric.RuntimeEnvironment
import tech.apter.junit.jupiter.robolectric.RobolectricExtension
import java.io.File
import kotlin.io.path.pathString

@OptIn(ExperimentalCoroutinesApi::class)
//@ExtendWith(RobolectricExtension::class)
class UserPreferencesDataSourceTest {

    @TempDir(cleanup = CleanupMode.ALWAYS)
    private lateinit var tempDir: File

    private lateinit var dataStore: DataStore<UserPreferences>

    private lateinit var userPreferences: UserPreferencesDataSource

    private lateinit var testScope: TestScope

    private lateinit var testDispatcher: TestDispatcher

    @BeforeEach
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        testScope = TestScope(testDispatcher + Job())
        Dispatchers.setMain(testDispatcher)

        // robolectric temp dir
//        tempDir = File(RuntimeEnvironment.getTempDirectory().basePath.pathString)

        assertTrue(tempDir.isDirectory)
        println(this.javaClass.simpleName + ": tempDir = " + tempDir.canonicalPath)

        val storageName = "test_storage.protobuf"

        val prefName = "test_storage.preferences"
        val pref = PreferenceDataStoreFactory.create { File(tempDir, prefName) }
        assertTrue(File(tempDir, prefName).isFile)

        dataStore = DataStoreFactory.create(
            serializer = UserPreferencesSerializer(),
            produceFile = {
                File(tempDir, storageName)
            },
            scope = testScope,
        )
        assertTrue(File(tempDir, storageName).isFile)

        userPreferences = UserPreferencesDataSource(dataStore)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun getUserData() = testScope.runTest {
//        advanceUntilIdle()
        userPreferences.setDarkTheme(DarkTheme.DARK)
        val first = userPreferences.userData.first()
//        advanceUntilIdle()
        assertEquals(DarkTheme.DARK, first.darkTheme)

//        userPreferences.userData.test {
//            val item = awaitItem()
//            assertTrue(item.favoriteCharacterIds.isEmpty())
//            awaitComplete()
//        }
    }

    @Test
    fun setFavorite() = runTest(testDispatcher) {
        assertEquals(5, 2 + 3)
    }
//
//    @Test
//    fun unsetFavorite() = testScope.runTest {
//    }
//
//    @Test
//    fun setDarkTheme() = testScope.runTest {
//    }

}
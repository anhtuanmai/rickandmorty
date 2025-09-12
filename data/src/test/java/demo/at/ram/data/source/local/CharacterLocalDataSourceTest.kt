package demo.at.ram.data.source.local

import androidx.room.Room
import demo.at.ram.data.source.local.dao.CharacterDao
import demo.at.ram.data.source.local.entity.CharacterEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.RuntimeEnvironment
import tech.apter.junit.jupiter.robolectric.RobolectricExtension
import kotlin.random.Random

@ExtendWith(RobolectricExtension::class)
class CharacterLocalDataSourceTest {

    private lateinit var database: AppDatabase
    private lateinit var characterDao: CharacterDao
    private lateinit var localDS: CharacterLocalDataSource

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        characterDao = database.characterDao()

        localDS = CharacterLocalDataSource(characterDao)
    }

    @Test
    fun saveAndLoadCharacters() = runTest {
        // When 1 : robolectric database is empty at Start
        val mustEmpty = localDS.loadCharacters()

        // Then 1
        assertEquals(0, mustEmpty.size)

        // Given 2
        val random = Random(36).nextLong(99999)
        val expectedCharacters = listOf(
            CharacterEntity(id = random, name = "Rick Sanchez"),
            CharacterEntity(id = random + 1, name = "Morty Smith")
        )

        // When 2
        localDS.saveCharacters(expectedCharacters)
        val characters = localDS.loadCharacters()

        // Then 2
        assertEquals(expectedCharacters, characters)
    }

    @AfterEach
    fun closeDatabase() {
        database.close()
    }

}
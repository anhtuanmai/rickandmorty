package demo.at.ram.data.source.local.dao

import androidx.room.Room
import demo.at.ram.data.source.local.AppDatabase
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
class CharacterDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var characterDao: CharacterDao

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        characterDao = database.characterDao()
    }

    @Test
    fun getAll() = runTest {
        // Given
        val random = Random(36).nextLong(99999)
        val expectedCharacters = listOf(
            CharacterEntity(id = random, name = "Rick Sanchez"),
            CharacterEntity(id = random + 1, name = "Morty Smith")
        )

        // When
        characterDao.insertAll(expectedCharacters)
        val characters = characterDao.getAll()

        // Then
        assertEquals(expectedCharacters, characters)
    }

    @Test
    fun insertAll() = runTest {
        // Given
        val random = Random(53).nextLong(99999)
        val expectedCharacters = listOf(
            CharacterEntity(id = random, name = "Rick Sanchez"),
            CharacterEntity(id = random + 1, name = "Morty Smith")
        )

        // When
        val insertedIds = characterDao.insertAll(expectedCharacters)

        // Then
        assertEquals(expectedCharacters.size, insertedIds.size)
    }

    @AfterEach
    fun closeDatabase() {
        database.close()
    }
}
package demo.at.ram.data.source.local

import androidx.datastore.core.DataStore
import demo.at.ram.data.source.local.dao.CharacterDao
import demo.at.ram.data.source.local.datastore.UserPreferences
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local character data source
 */
class CharacterLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao,
) {

    suspend fun loadCharacters() : List<CharacterEntity> {
        return characterDao.getAll()
    }

    suspend fun saveCharacters(characters: List<CharacterEntity>) : List<Long> {
        return characterDao.insertAll(characters)
    }

}
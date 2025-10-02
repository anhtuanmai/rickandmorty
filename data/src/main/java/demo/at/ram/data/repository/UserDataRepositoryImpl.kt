package demo.at.ram.data.repository

import demo.at.ram.data.source.local.UserPreferencesDataSource
import demo.at.ram.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    override fun getFavorites(): Flow<List<Long>> {
        val ids = userPreferencesDataSource.userData
            .map { it.favoriteCharacterIds }
            .onEach { Timber.d("ids : $it") }
        return ids
    }

    override suspend fun addFavorite(characterId: Long) {
        Timber.d("addFavorite : $characterId")
        userPreferencesDataSource.setFavorite(characterId)
    }

    override suspend fun removeFavorite(characterId: Long) {
        Timber.d("removeFavorite : $characterId")
        userPreferencesDataSource.unsetFavorite(characterId)
    }
}
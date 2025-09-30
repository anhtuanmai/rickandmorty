package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.UserPreferencesDataSource
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.UserDataRepository
import demo.at.ram.shared.di.ApplicationScope
import demo.at.ram.shared.dispatcher.Dispatcher
import demo.at.ram.shared.dispatcher.RamDispatchers.IO
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val characterLocalDataSource: CharacterLocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : UserDataRepository {
    override fun getFavorites(): Flow<ResponseResult<List<Character>>> {
        return userPreferencesDataSource.userData.combine(characterLocalDataSource.loadCharacters()) { userData ->

        }
    }

    override suspend fun addFavorite(character: Character) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(character: Character) {
        TODO("Not yet implemented")
    }
}
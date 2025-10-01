package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.di.ApplicationScope
import demo.at.ram.shared.dispatcher.Dispatcher
import demo.at.ram.shared.dispatcher.RamDispatchers.IO
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    override suspend fun getAllCharacters(): ResponseResult<List<Character>> {
        Timber.d("getAllCharacters")
        val wrapper = remoteDataSource.getAllCharacters()
        val characters = wrapper.response?.body()?.results
        if (wrapper.isSuccessful()) {
            cache(characters)
            return (
                ResponseResult.success(
                    code = wrapper.response?.code(),
                    data = characters ?: emptyList()
                )
            )
        } else {
            return (loadCharactersFromDb(wrapper.response?.code()))
        }
    }

    override suspend fun getSavedCharacters(): List<Character> {
        Timber.d("getSavedCharacters")
        return localDataSource.loadCharacters().map { it.toDomainModel() }
    }

    override suspend fun getCharacter(id: Long): Character? {
        Timber.d("getCharacter")
        return localDataSource.loadCharacters().find { it.id == id }?.toDomainModel()
    }

    private fun cache(characters: List<Character>?) {
        applicationScope.launch(ioDispatcher) {
            characters?.let {
                localDataSource.saveCharacters(it.map { CharacterEntity(it) })
            } ?: run {
                Timber.e("No data found in response")
            }
        }
    }

    private suspend fun loadCharactersFromDb(code: Int?): ResponseResult<List<Character>> {
        val characters = localDataSource.loadCharacters().map { it.toDomainModel() }
        return if (characters.isNotEmpty()) {
            ResponseResult.success(code = null, data = characters)
        } else {
            ResponseResult(isSuccessful = false, httpCode = code, data = null)
        }
    }

}
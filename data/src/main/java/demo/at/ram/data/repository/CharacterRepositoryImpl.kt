package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.di.ApplicationScope
import demo.at.ram.shared.dispatcher.Dispatcher
import demo.at.ram.shared.dispatcher.RamDispatchers
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Dispatcher(RamDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllCharacters(): Flow<ResponseResult<List<Character>>> =
        flow {
            emit(remoteDataSource.getAllCharacters())
        }
            .flatMapLatest { wrapper ->
                val characters = wrapper.response?.body()?.results
                if (wrapper.isSuccessful()) {
                    cache(characters)
                    flow {
                        emit(
                            ResponseResult.success(
                                code = wrapper.response?.code(),
                                data = characters ?: emptyList()
                            )
                        )
                    }
                } else {
                    flow {
                        emit(loadCharactersFromDb(wrapper.response?.code()))
                    }
                }
            }

    override fun getSavedCharacters(): Flow<List<Character>> {
        return flow {
            emit(localDataSource.loadCharacters().map { it.toDomainModel() })
        }
    }

    override fun getCharacter(id: Long): Flow<Character> {
        Timber.d("getCharacter")
        return flow {
            emit(localDataSource.loadCharacters().first { it.id == id }.toDomainModel())
        }
    }

    private fun cache(characters: List<Character>?) =
        applicationScope.launch(ioDispatcher) {
            characters?.let {
                localDataSource.saveCharacters(it.map { CharacterEntity(it) })
            } ?: run {
                Timber.e("No data found in response")
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
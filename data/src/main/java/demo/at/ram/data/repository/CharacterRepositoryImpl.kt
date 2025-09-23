package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.local.entity.CharacterEntity
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.SourceOrigin
import demo.at.ram.shared.model.ResponseResult
import timber.log.Timber
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
) : CharacterRepository {
    override suspend fun getAllCharacters(): ResponseResult<out List<Character>> {
        val remoteResponse = remoteDataSource.getAllCharacters()
        val result : ResponseResult<out List<Character>>
        if (remoteResponse.isSuccessful) {
            Timber.d("Remote data loaded successfully.")
            result = ResponseResult.success(
                code = remoteResponse.code(),
                data = remoteResponse.body()?.results?: emptyList()
            )
            remoteResponse.body()?.results?.let {
                val entities = it.map { CharacterEntity(it) }
                val saved = localDataSource.saveCharacters(entities)
                Timber.d("Saved ${saved.size}/${it.size} characters.")
            } ?: {
                Timber.w("Remote data is empty.")
            }
        } else {
            Timber.w("Remote data loading failed. Try to load from local db.")
            val localCharacters = localDataSource.loadCharacters()
            result = if (localCharacters.isNotEmpty()) {
                Timber.d("Loaded ${localCharacters.size} from local db.")
                ResponseResult(
                    isSuccessful = true,
                    remoteCode = remoteResponse.code(),
                    data = localCharacters.map { it.toDomainModel() },
                    sourceOrigin = SourceOrigin.LOCAL
                )
            } else {
                Timber.d("No character found from local db.")
                ResponseResult(
                    isSuccessful = false,
                    remoteCode = remoteResponse.code(),
                    data = null,
                )
            }
        }
        return result
    }

    override suspend fun getSavedCharacters(): List<Character> {
        return localDataSource.loadCharacters().map { it.toDomainModel() }
    }
}
package demo.at.ram.data.repository

import demo.at.ram.data.source.local.CharacterLocalDataSource
import demo.at.ram.data.source.remote.CharacterRemoteDataSource
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.EnumResponseStatus
import demo.at.ram.shared.model.ResponseResult
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
) : CharacterRepository {
    override suspend fun getAllCharacters(): ResponseResult<out List<Character>> {
        val response = remoteDataSource.getAllCharacters()
        return when (response.code()) {
            in 200..299 ->
                ResponseResult(
                    status = EnumResponseStatus.SUCCESS,
                    code = response.code(),
                    data = response.body()?.results
                )

            in 400..499 ->
                ResponseResult(
                    status = EnumResponseStatus.ERROR_REJECTED, code = response.code()
                )

            in 500..599 ->
                ResponseResult(
                    status = EnumResponseStatus.ERROR_NO_NETWORK,
                    code = response.code()
                )

            else ->
                ResponseResult(status = EnumResponseStatus.ERROR_UNKNOWN, code = response.code())
        }
    }

    override suspend fun getSavedCharacters(): List<Character> {
        return localDataSource.loadCharacters().map { it.toDomainModel() }
    }

}
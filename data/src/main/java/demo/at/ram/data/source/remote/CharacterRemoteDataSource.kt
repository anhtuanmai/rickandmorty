package demo.at.ram.data.source.remote

import demo.at.ram.data.source.remote.model.RestBody
import demo.at.ram.domain.model.Character
import retrofit2.Response
import javax.inject.Inject

/**
 * Remote character data source
 */
class CharacterRemoteDataSource @Inject constructor(private val ramService: RamService) {
    suspend fun getAllCharacters(): Response<RestBody<Character>> {
        return ramService.getAllCharacters()
    }
}
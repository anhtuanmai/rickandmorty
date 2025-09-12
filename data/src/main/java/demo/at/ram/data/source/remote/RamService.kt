package demo.at.ram.data.source.remote

import demo.at.ram.data.source.remote.model.RestBody
import demo.at.ram.domain.model.CharacterEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RamService {

    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int = 1): Response<RestBody<CharacterEntity>>
}
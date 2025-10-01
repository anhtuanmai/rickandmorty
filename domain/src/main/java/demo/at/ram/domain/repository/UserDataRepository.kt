package demo.at.ram.domain.repository

import demo.at.ram.domain.model.Character
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    suspend fun getFavorites(): List<Long>

    suspend fun addFavorite(characterId: Long)

    suspend fun removeFavorite(characterId: Long)
}
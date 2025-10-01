package demo.at.ram.domain.repository

import demo.at.ram.domain.model.Character
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    /**
     * Get all characters from remote
     */
    suspend fun getAllCharacters(): ResponseResult<List<Character>>

    /**
     * Get all characters from local
     */
    suspend fun getSavedCharacters(): List<Character>

    suspend fun getCharacter(id: Long): Character?
}
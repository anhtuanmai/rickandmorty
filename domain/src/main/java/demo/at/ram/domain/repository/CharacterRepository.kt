package demo.at.ram.domain.repository

import demo.at.ram.domain.model.CharacterEntity
import demo.at.ram.shared.model.ResponseResult

interface CharacterRepository {
    /// Get all characters from remote
    suspend fun getAllCharacters(): ResponseResult<out List<CharacterEntity>>

    /// Get all characters from local
    suspend fun getSavedCharacters(): List<CharacterEntity>
}
package demo.at.ram.domain.repository

import demo.at.ram.domain.model.CharacterEntity
import demo.at.ram.shared.model.Result

interface CharacterRepository {

    suspend fun getAllCharacters(): Result<out List<CharacterEntity>>
}
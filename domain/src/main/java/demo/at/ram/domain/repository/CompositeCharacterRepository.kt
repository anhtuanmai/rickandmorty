package demo.at.ram.domain.repository

import demo.at.ram.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CompositeCharacterRepository {
    fun getCharacter(id: Long): Flow<Character>
}
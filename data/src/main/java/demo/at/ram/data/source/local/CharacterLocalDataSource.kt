package demo.at.ram.data.source.local

import demo.at.ram.data.source.local.dao.CharacterDao

/**
 * Local character data source
 */
class CharacterLocalDataSource {

    suspend fun loadCharacters() : List<CharacterDao> {
        throw NotImplementedError()
    }

    suspend fun saveCharacters(characters: List<CharacterDao>) : Boolean {
        throw NotImplementedError()
    }
}
package demo.at.ram.presentation.di

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeCharacterRepository @Inject constructor() : CharacterRepository {

    private val characters = TestData.characters

    override fun getAllCharacters(): Flow<ResponseResult<List<Character>>> {
        return flow {
            emit(ResponseResult.success(characters))
        }
    }

    override fun getSavedCharacters(): Flow<List<Character>> {
        return flow {
            emit(characters)
        }
    }

    /**
     * @return search in [TestData.characters] or create new empty character if not found
     */
    override fun getCharacter(id: Long): Flow<Character> {
        return flow {
            emit(characters.find { it.id == id } ?: Character(id))
        }
    }
}
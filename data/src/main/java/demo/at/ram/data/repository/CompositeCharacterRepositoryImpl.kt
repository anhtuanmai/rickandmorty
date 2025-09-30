package demo.at.ram.data.repository

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.CompositeCharacterRepository
import demo.at.ram.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CompositeCharacterRepositoryImpl @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val userDataRepository: UserDataRepository,
) : CompositeCharacterRepository {
    override suspend fun getCharacter(id: Long): Flow<Character> {
        return combine(
            characterRepository.getSavedCharacters(),
            userDataRepository.getFavorites()
        ) { savedCharacters: List<Character>, favorites: List<Long> ->
            savedCharacters.first { favorites.contains(it.id) }
        }
    }
}
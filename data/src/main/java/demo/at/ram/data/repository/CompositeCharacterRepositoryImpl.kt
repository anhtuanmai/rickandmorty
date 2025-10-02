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

    override fun getFavorites(): Flow<List<Character>> =
        combine(
            flow = characterRepository.getSavedCharacters(),
            flow2 = userDataRepository.getFavorites()
        ) { characters, favorites ->
            characters.filter { it.id in favorites }
        }
}
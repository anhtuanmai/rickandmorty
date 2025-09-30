package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    val characterRepository: CharacterRepository,
    val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(): Flow<List<Character>> {
        return combine(
            characterRepository.getSavedCharacters(),
            userDataRepository.getFavorites()
        ) { savedCharacters: List<Character>, favorites: List<Long> ->
            savedCharacters.filter { favorites.contains(it.id) }
        }
    }
}
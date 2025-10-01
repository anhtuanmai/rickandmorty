package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.CompositeCharacterRepository
import demo.at.ram.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    val compositeCharacterRepository: CompositeCharacterRepository,
) {
    suspend operator fun invoke(): List<Character> {
        return compositeCharacterRepository.getFavorites()
    }
}
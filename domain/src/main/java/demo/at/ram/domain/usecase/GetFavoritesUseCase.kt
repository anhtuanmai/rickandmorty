package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CompositeCharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val compositeCharacterRepository: CompositeCharacterRepository,
) {
    operator fun invoke(): Flow<List<Character>> {
        return compositeCharacterRepository.getFavorites()
    }
}
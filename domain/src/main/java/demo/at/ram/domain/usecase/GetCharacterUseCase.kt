package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.CompositeCharacterRepository
import demo.at.ram.shared.model.ResponseResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(id: Long): Character? {
        val character = characterRepository.getCharacter(id)
        Timber.d("GetCharacterUseCase returned ${character?.name}")
        return character
    }
}
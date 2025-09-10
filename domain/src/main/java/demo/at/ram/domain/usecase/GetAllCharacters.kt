package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.CharacterEntity
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.Result
import javax.inject.Inject

class GetAllCharacters @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): Result<out List<CharacterEntity>> {
        return characterRepository.getAllCharacters()
    }
}
package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.ResponseResult
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): ResponseResult<out List<Character>> {
        return characterRepository.getAllCharacters()
    }
}
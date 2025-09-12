package demo.at.ram.domain.usecase

import demo.at.ram.domain.model.CharacterEntity
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.ResponseResult
import javax.inject.Inject

class GetAllCharacters @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): ResponseResult<out List<CharacterEntity>> {
        return characterRepository.getAllCharacters()
    }
}
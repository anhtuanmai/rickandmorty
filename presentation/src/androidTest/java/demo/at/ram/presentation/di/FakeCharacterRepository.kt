package demo.at.ram.presentation.di

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.Location
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.shared.model.ResponseResult
import javax.inject.Inject

class FakeCharacterRepository @Inject constructor() : CharacterRepository {

    private val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            location = Location("Earth", null),
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            location = Location("Earth", null),
        )
    )

    override suspend fun getAllCharacters(): ResponseResult<out List<Character>> {
        return ResponseResult.success(characters)
    }

    override suspend fun getSavedCharacters(): List<Character> {
        return characters
    }
}
package demo.at.ram.presentation.di

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.CharacterOrigin
import demo.at.ram.domain.model.Location

object TestData {

    val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = CharacterOrigin("Earth"),
            location = Location("Earth"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = arrayListOf("1", "2", "3"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z",
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            location = Location("Earth", null),
            origin = CharacterOrigin("Earth", null),
        )
    )
}
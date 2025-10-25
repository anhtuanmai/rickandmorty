package demo.at.ram.presentation.di

import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.Location

object TestData {

    val characters = listOf(
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
}
package demo.at.ram.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CharacterTest {

    @Test
    fun createNewInstance() {
        val character = Character(
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
        )

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals("Alive", character.status)
        assertEquals("Human", character.species)
        assertEquals("", character.type)
        assertEquals("Male", character.gender)
        assertEquals("Earth", character.origin?.name)
        assertEquals("Earth", character.location?.name)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", character.image)
        assertEquals(arrayListOf("1", "2", "3"), character.episode)
        assertEquals("https://rickandmortyapi.com/api/character/1", character.url)
        assertEquals("2017-11-04T18:48:46.250Z", character.created)
    }

}
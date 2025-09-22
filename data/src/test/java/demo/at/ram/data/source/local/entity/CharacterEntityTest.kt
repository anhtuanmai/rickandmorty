package demo.at.ram.data.source.local.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CharacterEntityTest {

    @Test
    fun toDomainModel() {
        // Given
        val expected = CharacterEntity(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )

        // When
        val domainModel = expected.toDomainModel()

        // Then
        assertEquals(expected.id, domainModel.id)
        assertEquals(expected.name, domainModel.name)
        assertEquals(expected.status, domainModel.status)
        assertEquals(expected.species, domainModel.species)
        assertEquals(expected.gender, domainModel.gender)
        assertEquals(expected.image, domainModel.image)
        assertEquals(expected.url, domainModel.url)
        assertEquals(expected.created, domainModel.created)

        // When
        val reverted = CharacterEntity(domainModel)

        // Then
        assertEquals(expected, reverted)
    }

}
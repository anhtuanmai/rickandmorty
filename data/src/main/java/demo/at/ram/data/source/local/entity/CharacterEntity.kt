package demo.at.ram.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import demo.at.ram.domain.model.Character

@Entity(tableName = CharacterEntity.TABLE_NAME)
class CharacterEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "species") val species: String? = null,
    @ColumnInfo(name = "gender") val gender: String? = null,
    @ColumnInfo(name = "image") val image: String? = null,
    @ColumnInfo(name = "url") val url: String? = null,
    @ColumnInfo(name = "created") val created: String? = null,
) {
    companion object {
        const val TABLE_NAME = "character_table"
    }

    constructor(character: Character) : this(
        id = character.id,
        name = character.name,
        status = character.status,
        species = character.species,
        gender = character.gender,
        image = character.image,
        url = character.url,
        created = character.created,
    )

    fun toDomainModel(): Character {
        return Character(
            id = id,
            name = name,
            status = status,
            species = species,
            gender = gender,
            image = image,
            url = url,
            created = created,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterEntity

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}

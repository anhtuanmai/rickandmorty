package demo.at.ram.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("species")
    var species: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("origin")
    var origin: CharacterOrigin? = CharacterOrigin(),
    @SerializedName("location")
    var location: Location? = Location(),
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("episode")
    var episode: ArrayList<String> = arrayListOf(),
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("created")
    var created: String? = null
)
package demo.at.ram.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterOrigin(
    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null
)
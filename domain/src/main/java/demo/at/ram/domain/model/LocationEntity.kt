package demo.at.ram.domain.model

import com.google.gson.annotations.SerializedName

data class LocationEntity(
    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null
)
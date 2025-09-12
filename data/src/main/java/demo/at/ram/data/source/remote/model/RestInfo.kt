package demo.at.ram.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class RestInfo(
    @SerializedName("count") val count: Int?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?,
)

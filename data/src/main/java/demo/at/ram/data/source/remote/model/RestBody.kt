package demo.at.ram.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class RestBody<out T>(
    @SerializedName("info") val info: RestInfo?,
    @SerializedName("results") val results: List<T>?,
)
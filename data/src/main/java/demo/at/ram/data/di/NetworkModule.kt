package demo.at.ram.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import demo.at.ram.data.source.remote.RamService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL: String = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun provideRamService(): RamService {
        val converterFactory = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
            .asConverterFactory("application/json".toMediaType())
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
            .create(RamService::class.java)
    }
}
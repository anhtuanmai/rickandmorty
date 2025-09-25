package demo.at.ram.data.di

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import demo.at.ram.data.source.remote.RamService
import demo.at.ram.data.util.ConnectivityManagerNetworkMonitor
import demo.at.ram.data.util.NetworkMonitor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    internal abstract fun bindNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ) : NetworkMonitor

    companion object {

        const val BASE_URL: String = "https://rickandmortyapi.com/api/"

        @Provides
        @Singleton
        fun provideRamService(@ApplicationContext context: Context): RamService {
            val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (isDebug) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // Retry on connection failures
                .build()

            @Suppress("JSON_FORMAT_REDUNDANT")
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(
                    Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                        .asConverterFactory("application/json".toMediaType())
                )
                .build()

            return retrofit.create(RamService::class.java)
        }
    }
}
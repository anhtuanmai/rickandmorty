package demo.at.ram.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import demo.at.ram.shared.di.ApplicationScope
import demo.at.ram.shared.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton
import demo.at.ram.shared.dispatcher.RamDispatchers.IO
import kotlinx.coroutines.CoroutineScope


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher)
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    }
}
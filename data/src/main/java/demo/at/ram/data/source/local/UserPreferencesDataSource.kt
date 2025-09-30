package demo.at.ram.data.source.local

import androidx.datastore.core.DataStore
import demo.at.ram.data.source.local.datastore.UserPreferences
import demo.at.ram.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    //TODO: implement
    val userData : Flow<UserData> =
        throw UnsupportedOperationException();
}
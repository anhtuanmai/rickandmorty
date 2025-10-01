package demo.at.ram.data.source.local

import androidx.datastore.core.DataStore
import demo.at.ram.data.source.local.datastore.DarkThemeConfig
import demo.at.ram.data.source.local.datastore.UserPreferences
import demo.at.ram.data.source.local.datastore.copy
import demo.at.ram.domain.model.DarkTheme
import demo.at.ram.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userData: Flow<UserData> =
        userPreferences.data
            .map {
                UserData(
                    darkTheme = parseDarkThemConfig(it.darkThemeConfig),
                    favoriteCharacterIds = it.favoriteCharacterIdsMap
                        .map { entry -> entry.key }
                )
            }

    suspend fun setFavorite(characterId: Long) {
        userPreferences.updateData {
            it.copy {
                favoriteCharacterIds.put(characterId, true)
            }
        }
    }

    suspend fun unsetFavorite(characterId: Long) {
        userPreferences.updateData {
            it.copy {
                favoriteCharacterIds.remove(characterId)
            }
        }
    }

    private fun parseDarkThemConfig(config: DarkThemeConfig): DarkTheme {
        return when (config) {
            DarkThemeConfig.DARK_THEME_CONFIG_LIGHT
                -> DarkTheme.LIGHT
            DarkThemeConfig.DARK_THEME_CONFIG_DARK
                -> DarkTheme.DARK
            DarkThemeConfig.DARK_THEME_CONFIG_UNSPECIFIED,
            DarkThemeConfig.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
            DarkThemeConfig.UNRECOGNIZED
                -> DarkTheme.FOLLOW_SYSTEM
        }
    }
}
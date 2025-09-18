package demo.at.ram.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import demo.at.ram.presentation.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val route: KClass<*>
) {
    HOME(
        label = R.string.home,
        icon = Icons.Default.Home,
        contentDescription = R.string.home,
        route = HomeBaseRoute::class
    ),
    FAVORITES(
        label = R.string.favorites,
        icon = Icons.Default.Favorite,
        contentDescription = R.string.favorites,
        route = FavoriteRoute::class
    ),
    ABOUT(
        label = R.string.about,
        icon = Icons.Default.Info,
        contentDescription = R.string.about,
        route = AboutRoute::class
    ),
}
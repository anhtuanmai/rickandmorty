package demo.at.ram.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import demo.at.ram.presentation.R

enum class TopLevelDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.home, Icons.Default.Home, R.string.home),
    FAVORITES(R.string.favorites, Icons.Default.Favorite, R.string.favorites),
    ABOUT(R.string.about, Icons.Default.Info, R.string.about),
}
package demo.at.ram.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import demo.at.ram.presentation.ui.RamAppState
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import demo.at.ram.presentation.ui.about.AboutScreen
import demo.at.ram.presentation.ui.details.DetailsScreen
import demo.at.ram.presentation.ui.favorites.FavoritesScreen
import demo.at.ram.presentation.ui.home.HomeScreen

//Top level
@Serializable data object HomeBaseRoute
@Serializable data object FavoriteRoute
@Serializable data object AboutRoute

//Nested
@Serializable data object HomeRoute
@Serializable data object DetailRoute

@Composable
fun RamNavHost(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = appState.navController,
        startDestination = HomeBaseRoute.toString(),
        modifier = modifier
    ) {
        navigation(route = HomeBaseRoute.toString(), startDestination = HomeRoute.toString()) {
            composable(route = HomeRoute.toString()) {
                HomeScreen()
            }
            composable(route = DetailRoute.toString()) {
                DetailsScreen()
            }
        }
        composable(route = FavoriteRoute.toString()) {
            FavoritesScreen()
        }
        composable(route = AboutRoute.toString()) {
            AboutScreen()
        }
    }
}
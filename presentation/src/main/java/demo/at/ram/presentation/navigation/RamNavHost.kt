package demo.at.ram.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import demo.at.ram.presentation.ui.RamAppState
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import demo.at.ram.presentation.ui.about.AboutScreen
import demo.at.ram.presentation.ui.favorites.FavoritesScreen
import demo.at.ram.presentation.ui.home.HomeScreen

@Serializable data object HomeBaseRoute // route to base navigation graph

@Serializable data object HomeRoute // route to base navigation graph

@Serializable data object FavoritesRoute // route to base navigation graph

@Composable
fun RamNavHost(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.HOME.name,
        modifier = modifier
    ) {
        composable(route = TopLevelDestination.HOME.name) {
            HomeScreen()
        }
        composable(route = TopLevelDestination.FAVORITES.name) {
            FavoritesScreen()
        }
        composable(route = TopLevelDestination.ABOUT.name) {
            AboutScreen()
        }
    }
}
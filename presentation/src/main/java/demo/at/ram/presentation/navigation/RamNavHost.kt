package demo.at.ram.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import demo.at.ram.presentation.ui.RamAppState
import demo.at.ram.presentation.ui.about.AboutScreen
import demo.at.ram.presentation.ui.details.DetailsScreen
import demo.at.ram.presentation.ui.favorites.FavoritesScreen
import demo.at.ram.presentation.ui.home.HomeScreen
import kotlinx.serialization.Serializable

//Top level
@Serializable
data object HomeBaseRoute
@Serializable
data object FavoriteRoute
@Serializable
data object AboutRoute

//Nested
@Serializable
data object HomeRoute
@Serializable
data object DetailRoute

@Composable
fun RamNavHost(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier
    ) {
        navigation<HomeBaseRoute>(startDestination = HomeRoute) {
            composable<HomeRoute> {
                HomeScreen(
                    goToCharacterDetails = { navController.navigate(DetailRoute) }
                )
            }
            composable<DetailRoute> {
                DetailsScreen()
            }
        }
        composable<FavoriteRoute> {
            FavoritesScreen()
        }
        composable<AboutRoute> {
            AboutScreen()
        }
    }
}
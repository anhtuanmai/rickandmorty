package demo.at.ram.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import demo.at.ram.presentation.ui.RamAppState
import demo.at.ram.presentation.ui.about.AboutScreen
import demo.at.ram.presentation.ui.details.DetailsScreen
import demo.at.ram.presentation.ui.details.DetailsViewModel
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
data class DetailRoute(val characterId: Long)

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
                    goToCharacterDetails = { navController.navigate(DetailRoute(it)) }
                )
            }
            composable<DetailRoute> { backStackEntry ->
                val id = backStackEntry.toRoute<DetailRoute>().characterId.toString()
                DetailsScreen(
                    viewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.Factory>(
                        key = id
                    ){ factory ->
                        factory.create(id)
                    }
                )
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
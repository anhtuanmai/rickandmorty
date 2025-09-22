package demo.at.ram.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    RamAppState(navController)
}

@Stable
class RamAppState(
    val navController: NavHostController
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable
        get() {
            val currentEntry = navController.currentBackStackEntryFlow.collectAsState(initial = null)
            if (currentEntry.value == null) {
                return previousDestination.value
            } else {
                previousDestination.value = currentEntry.value?.destination
                return currentEntry.value?.destination
            }
        }
}
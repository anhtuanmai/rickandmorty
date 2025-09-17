package demo.at.ram.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import demo.at.ram.presentation.navigation.TopLevelDestination
import demo.at.ram.presentation.navigation.RamNavHost
import timber.log.Timber
import kotlin.reflect.KClass

@Composable
fun RamApp(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    val currentDestination = appState.currentDestination
    Scaffold(
        modifier = modifier

    ) { contentPadding ->
        NavigationRail(modifier = Modifier.padding(contentPadding)) {
            TopLevelDestination.entries.forEachIndexed{ index, destination ->
                NavigationRailItem(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = stringResource(destination.contentDescription)
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                    selected = currentDestination.isRouteInHierarchy(destination::class),
                    onClick = {
                        Timber.i("onClick : ${destination.name}")
                        appState.navController.navigate(destination.name)
                    }
                )
            }
        }
        RamNavHost(
            appState = appState
        )
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true

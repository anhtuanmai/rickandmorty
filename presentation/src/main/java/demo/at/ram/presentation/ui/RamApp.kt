package demo.at.ram.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import demo.at.ram.presentation.R
import demo.at.ram.presentation.navigation.RamNavHost
import demo.at.ram.presentation.navigation.TopLevelDestination
import timber.log.Timber

@Composable
fun RamApp(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val notConnectedMessage = stringResource(R.string.no_connection)

    LaunchedEffect(isOffline) {
        Timber.d("LaunchedEffect : isOffline: $isOffline")
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = Indefinite,
            )
        }
    }

    val currentDestination = appState.currentDestination
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
            )
        },
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = stringResource(destination.contentDescription)
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination.isRouteInHierarchy(destination.route),
                        onClick = { appState.navController.navigate(destination.route) }
                    )
                }
            }
        }
    ) { contentPadding ->
        RamNavHost(
            appState = appState,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

private fun NavDestination?.isRouteInHierarchy(route: Any) =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true

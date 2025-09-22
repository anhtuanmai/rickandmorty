package demo.at.ram.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import demo.at.ram.presentation.ui.RamApp
import demo.at.ram.presentation.ui.rememberAppState
import demo.at.ram.presentation.designsystem.theme.RickAndMortyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val appState = rememberAppState()

            RickAndMortyTheme {
                RamApp(appState)
            }
        }
    }

}
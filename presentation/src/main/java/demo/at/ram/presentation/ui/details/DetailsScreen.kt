package demo.at.ram.presentation.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import demo.at.ram.presentation.designsystem.view.RamIconToggleButton
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * @param viewModel ViewModel injected via [DetailsViewModel.Factory]
 */
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    Timber.i("LaunchedEffect : Before = ${viewModel.detailsUiState.value}")

    val detailsUiState = viewModel.detailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.characterId) {
        Timber.i("LaunchedEffect : characterId = ${viewModel.characterId}")
        Timber.i("LaunchedEffect : On Triggered : state = ${viewModel.detailsUiState.value}")
//        viewModel.getCharacter(viewModel.characterId)
    }

    Card {
        Box {
            RamIconToggleButton(
                checked = true,
                onCheckedChange = viewModel::toggleFavorite,
                icon = { Icon(Icons.Default.FavoriteBorder, null) },
                checkedIcon = { Icon(Icons.Default.Favorite, null) },
            )
        }
    }
    Text(text = "Details Screen : ${viewModel.characterId} : ${detailsUiState.value}")
}


package demo.at.ram.presentation.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import demo.at.ram.presentation.designsystem.view.RamIconToggleButton
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import demo.at.ram.presentation.designsystem.view.ImageWithStates
import demo.at.ram.presentation.ui.log.LogCompositions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * @param viewModel ViewModel injected via [DetailsViewModel.Factory]
 */
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    LogCompositions("DetailsScreen")

    val detailsUiState = viewModel.detailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Timber.i("LaunchedEffect : characterId = ${viewModel.characterId}")
        Timber.i("LaunchedEffect : On Triggered : state = ${viewModel.detailsUiState.value}")
        viewModel.getCharacter()
    }

    if (detailsUiState.value is DetailsUiState.Success) {
        LogCompositions("DetailsUiState.Success")
        val character = (detailsUiState.value as DetailsUiState.Success).character
        val isFavorite = (detailsUiState.value as DetailsUiState.Success).isFavorite
        Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                RamIconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = viewModel::toggleFavorite,
                    icon = { Icon(Icons.Default.FavoriteBorder, null) },
                    checkedIcon = { Icon(Icons.Default.Favorite, null) },
                )
            }
            Column {
                ImageWithStates(character.image, Modifier.width(200.dp))
                Text(text = "Name : ${character.name}")
                Text(text = "Status : ${character.status}")
                Text(text = "Species : ${character.species}")
                Text(text = "Gender : ${character.gender}")
                Text(text = "Origin : ${character.origin?.name}")
                Text(text = "Location : ${character.location?.name}")
            }
        }
    } else {
        LogCompositions(detailsUiState.value.toString())
    }
}


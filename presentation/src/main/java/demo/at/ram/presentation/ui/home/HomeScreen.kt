package demo.at.ram.presentation.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import demo.at.ram.domain.model.Character
import timber.log.Timber

@Composable
fun HomeScreen(
    goToCharacterDetails: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        Timber.i("LaunchedEffect : viewModel.getAllCharacters")
        viewModel.getAllCharacters()
    }

    HomeContent(uiState.value, goToCharacterDetails)
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onCharacterClick: () -> Unit,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Text(text = "Loading")
        }

        is HomeUiState.Error -> {
            Text(text = "Loading")
        }

        is HomeUiState.Success -> {
            CharacterCardList(
                characters = uiState.characters,
                onCharacterClick = onCharacterClick
            )
        }
    }
}


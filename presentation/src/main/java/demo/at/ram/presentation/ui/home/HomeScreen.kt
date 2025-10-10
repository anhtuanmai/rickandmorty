package demo.at.ram.presentation.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import demo.at.ram.domain.error.AppError

@Composable
fun HomeScreen(
    goToCharacterDetails: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onCharacterClick = { characterId ->
            viewModel.onCharacterClick(characterId)
            goToCharacterDetails(characterId)
        },
        stringResolve = viewModel::getString
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onCharacterClick: (Long) -> Unit,
    stringResolve: AppError.() -> String,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Text(text = "Loading")
        }

        is HomeUiState.Error -> {
            Text(text = stringResolve(uiState.error))
        }

        is HomeUiState.Success -> {
            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = onRefresh,
            ) {
                CharacterCardList(
                    characters = uiState.characters,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}


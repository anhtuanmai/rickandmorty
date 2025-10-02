package demo.at.ram.presentation.ui.favorites

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import demo.at.ram.presentation.ui.home.CharacterCardList
import demo.at.ram.presentation.ui.log.LogCompositions

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    LogCompositions("FavoritesScreen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesContent(
        uiState = uiState,
        unsetFavorite = viewModel::unsetFavorite,
    )
}

@Composable
fun FavoritesContent(uiState: FavoritesUiState, unsetFavorite: (Long) -> Unit) {
    LogCompositions("FavoritesContent")

    when (uiState) {
        is FavoritesUiState.Loading -> {Text("loading")}
        is FavoritesUiState.Empty -> {Text("no favorites")}
        is FavoritesUiState.Success -> {
            CharacterCardList(
                characters = uiState.characters,
                onCharacterClick = unsetFavorite
            )
        }
        is FavoritesUiState.Error -> {Text("error")}
    }
}
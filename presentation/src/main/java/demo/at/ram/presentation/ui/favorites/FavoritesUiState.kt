package demo.at.ram.presentation.ui.favorites

import demo.at.ram.domain.model.Character

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(val characters : List<Character>) : FavoritesUiState
    data class Error(val throwable: Throwable) : FavoritesUiState
}
package demo.at.ram.presentation.ui.details

import demo.at.ram.domain.model.Character

sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    data class Error(val throwable: Throwable) : DetailsUiState

    data class Success(val character: Character, val isFavorite: Boolean) : DetailsUiState
}
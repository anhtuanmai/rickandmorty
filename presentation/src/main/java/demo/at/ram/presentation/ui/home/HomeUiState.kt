package demo.at.ram.presentation.ui.home

import demo.at.ram.domain.model.Character

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Error : HomeUiState
    data class Success(val characters: List<Character>) : HomeUiState
}
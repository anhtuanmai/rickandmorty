package demo.at.ram.presentation.ui.home

import demo.at.ram.domain.error.AppError
import demo.at.ram.domain.model.Character

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Error(val error: AppError) : HomeUiState
    data class Success(val characters: List<Character>, val noNetwork: Boolean) : HomeUiState
}
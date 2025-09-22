package demo.at.ram.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.usecase.GetAllCharactersUseCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getAllCharacters() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = getAllCharactersUseCase.invoke()
            if (result.isSuccessful) {
                _uiState.value = HomeUiState.Success(result.data!!)
            } else {
                _uiState.value = HomeUiState.Error
            }
        }
    }
}
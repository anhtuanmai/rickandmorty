package demo.at.ram.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : ViewModel() {
    val uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Success)
}

sealed interface MainActivityUiState {
    object Success : MainActivityUiState
    object Error : MainActivityUiState
    object Loading : MainActivityUiState

    fun shouldKeepSplashScreen() = this is Loading
}
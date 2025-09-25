package demo.at.ram.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.error.AppError
import demo.at.ram.domain.error.ErrorMessageResolver
import demo.at.ram.domain.usecase.GetAllCharactersUseCase
import demo.at.ram.shared.model.SourceOrigin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val errorMessageResolver: ErrorMessageResolver,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getAllCharacters() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = getAllCharactersUseCase()
            if (result.isSuccessful) {
                if (result.data.isNullOrEmpty()) {
                    _uiState.value = HomeUiState.Error(AppError.GeneralError.UnexpectedError)
                } else {
                    _uiState.value = HomeUiState.Success(
                        characters = result.data!!,
                        noNetwork = result.sourceOrigin == SourceOrigin.LOCAL
                    )
                }
            } else {
                result.httpCode?.let {
                    _uiState.value = HomeUiState.Error(AppError.NetworkError.HttpError(it))
                } ?: {
                    _uiState.value = HomeUiState.Error(AppError.GeneralError.UnexpectedError)
                }
            }
        }
    }

    fun onCharacterClick(characterId: Long) {
        Timber.i("onCharacterClick : $characterId")

    }

    fun getString(error: AppError): String = errorMessageResolver.resolveErrorMessage(error)
}
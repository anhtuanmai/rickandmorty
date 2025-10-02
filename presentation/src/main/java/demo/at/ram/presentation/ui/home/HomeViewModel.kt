package demo.at.ram.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.error.AppError
import demo.at.ram.domain.error.ErrorMessageResolver
import demo.at.ram.domain.error.toAppError
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.usecase.GetAllCharactersUseCase
import demo.at.ram.shared.model.ResponseResult
import demo.at.ram.shared.model.SourceOrigin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val errorMessageResolver: ErrorMessageResolver,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> =
        refreshTrigger
            .flatMapLatest {
                Timber.d("refreshTrigger flatMapLatest")
                getAllCharactersUseCase.invoke()
            }
            .map<ResponseResult<List<Character>>, HomeUiState> { wrapper ->
                if (wrapper.isSuccessful) {
                    HomeUiState.Success(
                        characters = wrapper.data ?: emptyList(),
                        noNetwork = wrapper.sourceOrigin == SourceOrigin.LOCAL
                    )
                } else {
                    HomeUiState.Error(getAppError(wrapper.httpCode))
                }
            }
            .catch { emit(HomeUiState.Error(it.toAppError())) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState.Loading
            )

    private fun getAppError(httpCode: Int?): AppError {
        return if (httpCode == null) {
            AppError.GeneralError.UnexpectedError
        } else {
            AppError.NetworkError.HttpError(httpCode)
        }
    }

    fun onCharacterClick(characterId: Long) {
        Timber.i("onCharacterClick : $characterId")
    }

    fun refresh() {
        Timber.i("refresh")
        // Emit Unit to trigger refresh
        refreshTrigger.value += 1
    }

    fun getString(error: AppError): String = errorMessageResolver.resolveErrorMessage(error)
}
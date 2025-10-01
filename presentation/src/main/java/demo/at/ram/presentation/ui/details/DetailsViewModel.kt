package demo.at.ram.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.repository.UserDataRepository
import demo.at.ram.domain.usecase.GetCharacterUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    val getCharacterUseCase: GetCharacterUseCase,
    val userDataRepository: UserDataRepository,
    @Assisted injectedId: String,
) : ViewModel() {

    val characterId: Long = injectedId.toLong()

    fun toggleFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                userDataRepository.addFavorite(characterId)
            } else {
                userDataRepository.removeFavorite(characterId)
            }
        }
    }

    val detailsUiState: StateFlow<DetailsUiState> =
        flow {
            emit(getCharacterUseCase.invoke(characterId))
        }
            .map<Character?, DetailsUiState> {
                it?.let {
                    DetailsUiState.Success(it)
                } ?: run {
                    DetailsUiState.Error(Throwable("Character not found"))
                }
            }
            .catch { emit(DetailsUiState.Error(it)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailsUiState.Loading
            )

    @AssistedFactory
    interface Factory {
        fun create(characterId: String): DetailsViewModel
    }
}
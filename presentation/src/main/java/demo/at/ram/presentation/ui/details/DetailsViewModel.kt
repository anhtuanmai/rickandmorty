package demo.at.ram.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.repository.UserDataRepository
import demo.at.ram.domain.usecase.GetCharacterUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    val getCharacterUseCase: GetCharacterUseCase,
    val userDataRepository: UserDataRepository,
    @Assisted injectedId: String,
) : ViewModel() {

    val characterId: Long = injectedId.toLong()

    fun toggleFavorite(isFavorite: Boolean) =
        viewModelScope.launch {
            Timber.d("isFavorite = $isFavorite")
            if (isFavorite) {
                userDataRepository.addFavorite(characterId)
            } else {
                userDataRepository.removeFavorite(characterId)
            }
        }

    val detailsUiState: StateFlow<DetailsUiState> =
        combine(
            flow = getCharacterUseCase.invoke(characterId),
            flow2 = userDataRepository.getFavorites()
        ) { character, favorites ->
            DetailsUiState.Success(
                character = character,
                isFavorite = favorites.contains(character.id)
            )
        }
            .distinctUntilChanged()
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
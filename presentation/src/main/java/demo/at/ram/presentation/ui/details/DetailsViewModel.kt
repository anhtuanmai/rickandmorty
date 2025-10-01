package demo.at.ram.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.repository.UserDataRepository
import demo.at.ram.domain.usecase.GetCharacterUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    val getCharacterUseCase: GetCharacterUseCase,
    val userDataRepository: UserDataRepository,
    @Assisted injectedId: String,
) : ViewModel() {

    val characterId: Long = injectedId.toLong()

    fun toggleFavorite(isFavorite: Boolean) {
        Timber.d("isFavorite = $isFavorite")
        viewModelScope.launch {
            if (isFavorite) {
                userDataRepository.addFavorite(characterId)
            } else {
                userDataRepository.removeFavorite(characterId)
            }
            getCharacter()
        }
    }

    private val _detailsUiState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState.Loading)
    val detailsUiState: StateFlow<DetailsUiState> = _detailsUiState.asStateFlow()

    fun getCharacter() {
        viewModelScope.launch {
            coroutineScope {
                val deferred1 = async { getCharacterUseCase(characterId) }
                val deferred2 = async { isFavorite(characterId) }
                val character = deferred1.await()
                Timber.i("getCharacter : character = ${character?.name}")
                val isFavorite = deferred2.await()
                Timber.i("getCharacter : isFavorite = $isFavorite")
                val nextUiState = if (character != null) {
                    DetailsUiState.Success(character = character, isFavorite = isFavorite)
                } else {
                    DetailsUiState.Error(Throwable("Character not found"))
                }
                _detailsUiState.emit(nextUiState)
            }
        }
    }

    private suspend fun isFavorite(characterId: Long): Boolean {
        val favorites = userDataRepository.getFavorites()
        return favorites.contains(characterId)
    }

    @AssistedFactory
    interface Factory {
        fun create(characterId: String): DetailsViewModel
    }
}
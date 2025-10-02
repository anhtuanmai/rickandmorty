package demo.at.ram.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.at.ram.domain.repository.UserDataRepository
import demo.at.ram.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> =
        getFavoritesUseCase.invoke()
            .map {
                if (it.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Success(it)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoritesUiState.Loading
            )

    fun unsetFavorite(characterId: Long) =
        viewModelScope.launch {
            userDataRepository.removeFavorite(characterId)
        }
}
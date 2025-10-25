package demo.at.ram.presentation.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.tools.screenshot.PreviewTest
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.CharacterOrigin
import demo.at.ram.domain.model.Location
import demo.at.ram.presentation.designsystem.theme.RickAndMortyTheme
import demo.at.ram.presentation.designsystem.view.ImageWithStates
import demo.at.ram.presentation.designsystem.view.RamIconToggleButton
import demo.at.ram.presentation.ui.log.LogCompositions
import demo.at.ram.shared.annotation.ExcludeFromJacocoGeneratedReport

/**
 * @param viewModel ViewModel injected via [DetailsViewModel.Factory]
 */
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    LogCompositions("DetailsScreen")

    val detailsUiState by viewModel.detailsUiState.collectAsStateWithLifecycle()

    DetailsContent(detailsUiState, viewModel::toggleFavorite)
}

@Composable
internal fun DetailsContent(detailsUiState: DetailsUiState, toggleFavorite: (Boolean) -> Unit) {
    if (detailsUiState is DetailsUiState.Success) {
        LogCompositions("DetailsUiState.Success")
        val character = detailsUiState.character
        val isFavorite = detailsUiState.isFavorite
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                LogCompositions("Box isFavorite")
                RamIconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = toggleFavorite,
                    icon = { Icon(Icons.Default.FavoriteBorder, null) },
                    checkedIcon = { Icon(Icons.Default.Favorite, null) },
                )
            }
            Character(character = character)
        }
    } else {
        LogCompositions(detailsUiState.toString())
    }
}

@Composable
internal fun Character(character: Character) {
    Column {
        LogCompositions("Column character")

        ImageWithStates(character.image, Modifier.width(200.dp))
        Text(text = "Name : ${character.name}")
        Text(text = "Status : ${character.status}")
        Text(text = "Species : ${character.species}")
        Text(text = "Gender : ${character.gender}")
        Text(text = "Origin : ${character.origin?.name}")
        Text(text = "Location : ${character.location?.name}")
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewTest
@Preview(showBackground = true, device = "id:pixel_7")
@Composable
fun DetailsScreenPreview() {
    RickAndMortyTheme {
        DetailsContent(
            DetailsUiState.Success(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    origin = CharacterOrigin("Earth", null),
                    location = Location("Earth", null),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                ),
                true
            )
        ) { isFavorite ->
            println("isFavorite = $isFavorite")
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun CharacterPreview() {
    RickAndMortyTheme {
        Character(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                location = Location("Earth", null),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            )
        )
    }
}

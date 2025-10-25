package demo.at.ram.presentation.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.tools.screenshot.PreviewTest
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.CharacterOrigin
import demo.at.ram.domain.model.Location
import demo.at.ram.presentation.R
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
    LogCompositions(detailsUiState.toString())

    Box(modifier = Modifier.testTag("details_screen")) {
        when (detailsUiState) {
            is DetailsUiState.Success -> {
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
            }

            is DetailsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is DetailsUiState.Error -> {
                Text(detailsUiState.throwable.message ?: "empty error message")
            }
        }
    }
}

@Composable
internal fun Character(character: Character) {
    Column {
        LogCompositions("Column character")

        ImageWithStates(character.image, Modifier.width(200.dp))

        CharacterInfo(
            label = stringResource(R.string.label_name),
            value = character.name,
            testTag = "character_name"
        )
        CharacterInfo(
            label = stringResource(R.string.label_status),
            value = character.status,
            testTag = "character_status"
        )
        CharacterInfo(
            label = stringResource(R.string.label_species),
            value = character.species,
            testTag = "character_species"
        )
        CharacterInfo(
            label = stringResource(R.string.label_gender),
            value = character.gender,
            testTag = "character_gender"
        )
        character.origin?.name?.let {
            CharacterInfo(
                label = stringResource(R.string.label_origin),
                value = it,
                testTag = "character_origin"
            )
        }
        character.location?.name?.let {
            CharacterInfo(
                label = stringResource(R.string.label_location),
                value = it,
                testTag = "character_location"
            )
        }
    }
}

@Composable
private fun CharacterInfo(
    label: String,
    value: String?,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label: ")
            }
            if (value != null) {
                append(value)
            }
        },
        modifier = modifier
            .testTag(testTag)
            .semantics(mergeDescendants = true) {
                contentDescription = "$label: $value"
            }
    )
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

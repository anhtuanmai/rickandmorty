package demo.at.ram.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import demo.at.ram.domain.model.Character
import demo.at.ram.domain.model.Location
import demo.at.ram.presentation.designsystem.theme.RickAndMortyTheme
import demo.at.ram.presentation.designsystem.view.ImageWithStates
import demo.at.ram.shared.annotation.ExcludeFromJacocoGeneratedReport

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier,
    onCharacterClick: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onCharacterClick(character.id) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row {
            ImageWithStates(character.image)
            Column {
                Text(text = character.name ?: "")
                Text(
                    text = character.status ?: "",
                    color = if (character.status == "Alive") Color.Green else Color.Red,
                )
                Text(text = character.location?.name ?: "")
            }
        }
    }
}

@Composable
fun CharacterCardList(
    characters: List<Character>,
    onCharacterClick: (Long) -> Unit
) {
    LazyColumn(Modifier.testTag("characterList")) {
        items(count = characters.size) { index ->
            CharacterCard(
                character = characters[index],
                modifier = Modifier.testTag("item_${characters[index].id}"),
                onCharacterClick = onCharacterClick
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun CharacterCardPreview() {
    RickAndMortyTheme {
        Box(
            Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            CharacterCard(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    location = Location("Earth", null),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                ),
                onCharacterClick = {}
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun CharacterCardListPreview() {
    RickAndMortyTheme {
        Box(
            Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            CharacterCardList(
                characters = listOf(
                    Character(
                        id = 1,
                        name = "Rick Sanchez",
                        status = "Alive",
                        location = Location("Earth", null),
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    Character(
                        id = 7,
                        name = "Abradolf Lincler",
                        status = "Dead",
                        location = Location("Testicle Monster Dimension", null),
                        image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg"
                    )
                ),
                onCharacterClick = {}
            )
        }
    }
}


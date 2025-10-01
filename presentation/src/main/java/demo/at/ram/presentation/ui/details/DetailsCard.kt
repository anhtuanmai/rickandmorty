package demo.at.ram.presentation.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import demo.at.ram.domain.model.Character
import demo.at.ram.presentation.designsystem.view.ImageWithStates

@Composable
fun CharacterCard(character: Character, onFavoriteClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onFavoriteClick(character.id) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Button(
                onClick = { onFavoriteClick(character.id) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            }
            Row {
                ImageWithStates(character.image)
                Column {
                    Text(text = character.name ?: "")
                    Text(
                        text = character.status ?: "",
                        color = if (character.status == "Alive") Color.Green else Color.Red,
                    )
                    Text(text = character.origin?.name ?: "")
                }
            }
        }
    }
}

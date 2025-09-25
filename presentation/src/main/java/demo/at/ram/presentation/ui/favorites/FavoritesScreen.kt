package demo.at.ram.presentation.ui.favorites

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FavoritesScreen() {
    Text(text = "Favorites Screen")

    Button(
        onClick = {
            throw RuntimeException("Test Crash") // Force a crash
        },
        content = { Text(text = "Crash me!") },
    )
}
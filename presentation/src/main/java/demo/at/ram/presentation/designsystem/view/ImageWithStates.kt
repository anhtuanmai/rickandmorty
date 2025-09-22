package demo.at.ram.presentation.designsystem.view

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import demo.at.ram.presentation.R

@Composable
fun ImageWithStates(
    imageUrl: String?, modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Remote image",
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.baseline_downloading_24),
        error = painterResource(R.drawable.baseline_error_24)
    )
}

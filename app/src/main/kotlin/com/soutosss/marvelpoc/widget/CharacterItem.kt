package com.soutosss.marvelpoc.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.soutosss.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.design.R

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
    onClick: (Character) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = modifier.clickable(onClick = { onClick(character) })) {

        Image(
            modifier = Modifier.fillMaxWidth(),
            url = character.thumbnailUrl,
            height = 150.dp
        )

        Row(
            modifier = Modifier
                .background(color = colorResource(id = R.color.gray_background))
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .testTag(character.name),
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            FavoriteToggle(
                isFavorite = character.favorite,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun FavoriteToggle(
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val resId =
        if (isFavorite) R.drawable.ic_favorite_24px else R.drawable.ic_favorite_border_24px
    val painter = rememberVectorPainter(ImageVector.vectorResource(id = resId))
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = onCheckedChange
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            tint = colorResource(id = R.color.colorPrimary)
        )
    }
}

@Composable
private fun Image(modifier: Modifier = Modifier, url: String, height: Dp) {
    val painterImage = rememberImagePainter(url)

    if (painterImage.state is ImagePainter.State.Loading) {
        Loading(modifier = modifier, heightIn = height)
    } else {
        Image(
            painter = painterImage,
            modifier = modifier
                .height(height),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier, heightIn: Dp) {
    Box(
        modifier = modifier
            .heightIn(heightIn),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.loading_page_size)))
    }
}

@Composable
@Preview("Character Item Preview")
private fun CharacterItemPreview() {
    val character = Character(id = 30L, name = "Souto", "httt", description = "", favorite = true)
    CharacterItem(modifier = Modifier.height(150.dp), character, {}) {}
}
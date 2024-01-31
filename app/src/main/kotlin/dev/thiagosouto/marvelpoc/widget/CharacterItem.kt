package dev.thiagosouto.marvelpoc.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.thiagosouto.marvelpoc.design.R
import dev.thiagosouto.marvelpoc.design.components.FavoriteToggle
import dev.thiagosouto.marvelpoc.design.components.ImageLoading
import dev.thiagosouto.marvelpoc.domain.model.Character

@Composable
internal fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
    onClick: (Character) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .clickable(onClick = { onClick(character) })
    ) {

        ImageLoading(
            modifier = Modifier.fillMaxWidth(),
            url = character.thumbnailUrl,
            height = 150.dp,
            contentScale = ContentScale.Crop
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
@Preview("Character Item Preview")
private fun CharacterItemPreview() {
    val character = Character(id = 30L, name = "Souto", "httt", description = "", favorite = true)
    CharacterItem(modifier = Modifier.height(150.dp), character, {}) {}
}

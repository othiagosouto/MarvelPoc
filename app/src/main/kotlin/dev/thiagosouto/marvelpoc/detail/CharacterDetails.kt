package dev.thiagosouto.marvelpoc.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.domain.model.Comics
import dev.thiagosouto.marvelpoc.design.components.ImageLoading
import dev.thiagosouto.marvelpoc.design.components.Loading
import dev.thiagosouto.marvelpoc.design.R as RDesign

@Composable
internal fun CharacterDetails(
    name: String,
    description: String,
    imageUrl: String,
    comics: List<Comics>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
    ) {
        ScrollableArea(imageUrl, name, description, comics)
    }

}

@Composable
internal fun ScrollableArea(
    imageUrl: String,
    name: String,
    description: String,
    comics: List<Comics>
) {
    ImageLoading(
        url = imageUrl,
        contentDescription = "",
        modifier = Modifier
            .heightIn(max = 400.dp)
            .fillMaxWidth(),
        height = 400.dp,
        contentScale = ContentScale.FillWidth,
    )
    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#25000000"))),
    ) {
        Text(
            text = name,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .testTag("name"),
        )
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = RDesign.dimen.spacing_medium),
                vertical = dimensionResource(id = RDesign.dimen.spacing_small)
            )
            .testTag("description"),
        text = description.ifBlank { stringResource(id = R.string.character_details_description_not_available) })

    LazyRow(modifier = Modifier.testTag("character-details-comics")) {
        this.itemsIndexed(comics) { index, item ->
            ComicsView(item, index)
        }
    }
}

@Composable
internal fun ComicsView(comics: Comics, index: Int) {
    val painterImage = rememberAsyncImagePainter(comics.thumbnailUrl)

    SubcomposeAsyncImage(
        model = comics.thumbnailUrl,
        loading = {
            val loadingSize = dimensionResource(id = RDesign.dimen.loading_page_size)
            Loading(modifier = Modifier
                .width(220.dp)
                .height(200.dp), loadingSize)
                  },
        contentDescription = "",
        success = {
            Column(modifier = Modifier.padding(dimensionResource(id = RDesign.dimen.spacing_small))) {
                Image(
                    painter = painterImage,
                    contentDescription = comics.title,
                    Modifier
                        .height(220.dp)
                        .width(150.dp),
                    contentScale = ContentScale.FillWidth,
                )

                Text(
                    text = comics.title,
                    modifier = Modifier
                        .width(150.dp)
                        .heightIn(48.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#25000000")))
                        .padding(2.dp)
                        .testTag("comics-title-$index"),
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

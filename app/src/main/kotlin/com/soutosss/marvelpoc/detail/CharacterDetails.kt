package com.soutosss.marvelpoc.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharacterDetails

@Composable
fun CharacterDetails(name: String, description: String, imageUrl: String) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
    ) {
        val painterImage = rememberImagePainter(imageUrl)
        if (painterImage.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        } else {
            Image(
                painter = painterImage,
                contentDescription = null,
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }
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
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .testTag("description"),
            text = description.ifBlank { stringResource(id = R.string.character_details_description_not_available) })
    }
}

@Composable
fun Loading() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.size(72.dp))
    }
}

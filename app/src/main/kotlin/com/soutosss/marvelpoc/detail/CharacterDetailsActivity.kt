package com.soutosss.marvelpoc.detail

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.model.view.Character
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsActivity : ComponentActivity() {

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val character: Character = intent.extras!!.getSerializable(CHARACTER_KEY) as Character
        characterDetailsViewModel.loadFavoriteData(character.id.toString())
        setContent {
            val characterDetails =
                characterDetailsViewModel.characterDetails.observeAsState(CharacterDetails.empty)
            if (characterDetails.value == CharacterDetails.empty) {
                Loading()
            } else {
                CharacterDetails(characterDetails.value)
            }
        }
    }

    companion object {
        const val CHARACTER_KEY = "CHARACTER_KEY"
        const val TAG = "CharacterDetailsFragment"
    }
}

@Composable
fun CharacterDetails(characterDetails: CharacterDetails) {
    var offset = remember { 0f }

    Column(modifier = Modifier.scrollable(state = rememberScrollableState { delta ->
        offset += delta
        delta
    }, orientation = Orientation.Vertical)) {
        val painterImage = rememberImagePainter(characterDetails.imageUrl)
        if (painterImage.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        } else {
            Image(
                painter = painterImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = androidx.compose.ui.graphics.Color(Color.parseColor("#25000000")))
        ) {
            Text(
                text = characterDetails.name,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            text = characterDetails.description.ifBlank { stringResource(id = R.string.character_details_description_not_available) })
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
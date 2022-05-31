package dev.thiagosouto.marvelpoc.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.design.components.LoadingPage
import dev.thiagosouto.marvelpoc.shared.mvi.MviView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsActivity : ComponentActivity(), MviView<DetailsViewState> {

    private var state by mutableStateOf<DetailsViewState>(DetailsViewState.Idle)
    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterDetailsViewModel.bind(this)

        if (savedInstanceState == null) {
            val character: Character = intent.extras!!.getSerializable(CHARACTER_KEY) as Character
            characterDetailsViewModel.process(Intent.OpenScreen(character.id))
        }

        setContent {
            CharacterDetailsScreen(
                characterDetailsViewModel::process,
                state
            )
        }
    }

    companion object {
        const val CHARACTER_KEY = "CHARACTER_KEY"
        const val TAG = "CharacterDetailsFragment"
    }

    override fun render(state: DetailsViewState) {
        this.state = state
    }
}

@Composable
fun CharacterDetailsScreen(
    process: (Intent) -> Unit,
    viewState: DetailsViewState
) {
    when (viewState) {
        is DetailsViewState.Loading -> LoadingPage()
        is DetailsViewState.Loaded -> LoadedViewState(viewState, process)
    }
}

@Composable
fun LoadedViewState(viewState: DetailsViewState.Loaded, process: (Intent) -> Unit) {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { process(Intent.CloseScreen) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(android.graphics.Color.WHITE)
                    )
                }
            },
            backgroundColor = colorResource(id = R.color.colorPrimary),
            elevation = 2.dp
        )

        CharacterDetails(
            viewState.name,
            viewState.description,
            viewState.imageUrl,
            viewState.comics
        )
    }
}

package dev.thiagosouto.marvelpoc.widget

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import dev.thiagosouto.domain.model.Character

internal class CharacterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {
    private var character: Character by mutableStateOf(Character.EMPTY)
    private lateinit var favoriteClick: (Character) -> Unit
    private var itemClick: ((Character) -> Unit) = {}

    fun updateCharacter(character: Character) {
        this.character = character
    }

    fun setListeners(favoriteClick: (Character) -> Unit, itemClick: ((Character) -> Unit) = {  }) {
        this.favoriteClick = favoriteClick
        this.itemClick = itemClick
    }

    @Composable
    override fun Content() {
        if (character == Character.EMPTY) return

        CharacterItem(modifier = Modifier, character, itemClick) {
            character = character.copy(favorite = it)
            character.let(favoriteClick)
        }
    }
}

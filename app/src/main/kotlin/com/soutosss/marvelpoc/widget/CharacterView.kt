package com.soutosss.marvelpoc.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character

class CharacterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val headerText by lazy { findViewById<TextView>(R.id.text) }
    private val imageView by lazy { findViewById<ImageView>(R.id.image) }
    private val header by lazy { findViewById<View>(R.id.header) }
    private val progress by lazy { findViewById<ContentLoadingProgressBar>(R.id.progressItem) }
    private val favoriteCheckBox by lazy { findViewById<CheckBox>(R.id.favorite) }
    private lateinit var character: Character
    private lateinit var favoriteClick: (Character) -> Unit
    private var itemClick: ((Character) -> Unit)? = null

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_character, this, true)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        favoriteCheckBox.setOnClickListener {
            character.favorite = character.favorite.not()
            character.let(favoriteClick)
        }
        this.setOnClickListener {
            itemClick?.invoke(character)
        }

        headerText.setOnClickListener {
            itemClick?.invoke(character)
        }
    }

    fun updateCharacter(character: Character) {
        this.character = character
        headerText.text = character.name
        loadHomeImage(
            imageView,
            character.thumbnailUrl,
            progress,
            favoriteCheckBox, headerText, header
        )
        favoriteCheckBox.isChecked = character.favorite
    }

    fun applyDetailMode() {
        imageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setPadding(0, 0, 0, 0)
        imageView.invalidate()
    }

    fun setListeners(favoriteClick: (Character) -> Unit, itemClick: ((Character) -> Unit)? = null) {
        this.favoriteClick = favoriteClick
        this.itemClick = itemClick
    }
}
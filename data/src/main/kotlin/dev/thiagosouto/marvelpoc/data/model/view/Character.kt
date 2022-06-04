package dev.thiagosouto.marvelpoc.data.model.view

import java.io.Serializable

/**
 * Ui model that represent Character
 */
data class Character(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    var favorite: Boolean
) : Serializable {

    companion object {
        const val serialVersionUID = 1L

        val EMPTY = Character(
            id = 0L,
            name = "",
            thumbnailUrl = "",
            description = "",
            favorite = false
        )
    }
}

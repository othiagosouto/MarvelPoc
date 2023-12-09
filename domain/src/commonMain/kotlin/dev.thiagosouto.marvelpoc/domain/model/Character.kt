package dev.thiagosouto.marvelpoc.domain.model

/**
 * Ui model that represent Character
 */
data class Character(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val favorite: Boolean
) {

    companion object {
        private const val serialVersionUID = 1L

        val EMPTY = Character(
            id = 0L,
            name = "",
            thumbnailUrl = "",
            description = "",
            favorite = false
        )
    }
}

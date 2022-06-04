package dev.thiagosouto.marvelpoc.data

/**
 * Character details domain model
 */
class CharacterDetails(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val comics: List<Comics>
)

/**
 * Comics domain model
 */
data class Comics(
    val id: Long,
    val title: String,
    val imageUrl: String
)

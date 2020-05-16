package com.soutosss.marvelpoc.data.room_source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soutosss.marvelpoc.data.model.view.Character

@Entity(tableName = CharacterLocal.TABLE_NAME)
data class CharacterLocal(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
) {

    companion object {
        const val TABLE_NAME = "character_local"
    }
}

fun CharacterLocal.toCharacter() =
    Character(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )
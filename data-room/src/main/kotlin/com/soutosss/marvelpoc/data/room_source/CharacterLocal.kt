package com.soutosss.marvelpoc.data.room_source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CharacterLocal.TABLE_NAME)
internal data class CharacterLocal(
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

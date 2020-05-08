package com.soutosss.marvelpoc.data.model.view

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soutosss.marvelpoc.data.model.character.Result

@Entity(tableName = CharacterHome.TABLE_NAME)
data class CharacterHome(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
) {
    constructor(response: Result) : this(
        id = response.id,
        name = response.name,
        thumbnailUrl = response.thumbnail.path + "." + response.thumbnail.extension,
        favorite = false
    )

    companion object{
        const val TABLE_NAME = "character_home"
    }
}
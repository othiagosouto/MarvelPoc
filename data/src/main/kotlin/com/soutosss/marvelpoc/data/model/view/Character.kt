package com.soutosss.marvelpoc.data.model.view

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soutosss.marvelpoc.data.model.character.Result
import java.io.Serializable

@Entity(tableName = Character.TABLE_NAME)
data class Character(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
): Serializable{
    constructor(response: Result) : this(
        id = response.id,
        name = response.name,
        thumbnailUrl = response.thumbnail.path + "." + response.thumbnail.extension,
        favorite = false,
        description =  response.description
    )

    companion object{
        const val TABLE_NAME = "character"
    }
}
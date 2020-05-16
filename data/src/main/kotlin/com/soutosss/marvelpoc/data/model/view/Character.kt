package com.soutosss.marvelpoc.data.model.view

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = Character.TABLE_NAME)
data class Character(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
): Serializable{
    companion object{
        const val TABLE_NAME = "character"
    }
}
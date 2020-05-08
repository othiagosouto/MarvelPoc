package com.soutosss.marvelpoc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soutosss.marvelpoc.data.model.view.CharacterHome

@Database(entities = [CharacterHome::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersHomeDAO(): CharacterHomeDAO
}

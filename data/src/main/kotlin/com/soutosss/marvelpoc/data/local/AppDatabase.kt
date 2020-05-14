package com.soutosss.marvelpoc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soutosss.marvelpoc.data.model.view.Character

@Database(entities = [Character::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDAO(): CharacterDAO
}

package dev.thiagosouto.marvelpoc.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterLocal::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersLocalDao(): CharacterLocalDAO
}

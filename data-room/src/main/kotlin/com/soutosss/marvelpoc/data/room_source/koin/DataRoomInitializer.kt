package com.soutosss.marvelpoc.data.room_source.koin

import androidx.room.Room
import com.soutosss.marvelpoc.data.room_source.AppDatabase
import com.soutosss.marvelpoc.data.room_source.BuildConfig
import com.soutosss.marvelpoc.data.room_source.CharacterLocal
import com.soutosss.marvelpoc.data.room_source.CharacterLocalRoomDataSource
import com.soutosss.marvelpoc.shared.contracts.character.CharacterLocalContract
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import org.koin.core.module.Module
import org.koin.dsl.module

class DataRoomInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(module {
            single {
                Room.databaseBuilder(
                    get(), AppDatabase::class.java, BuildConfig.DATABASE_NAME
                ).build().charactersLocalDao()
            }
            single {
                CharacterLocalRoomDataSource(
                    get()
                ) as CharacterLocalContract<CharacterLocal>
            }
        })
    }
}

package com.soutosss.marvelpoc.data.room.koin

import androidx.room.Room
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.room.AppDatabase
import com.soutosss.marvelpoc.data.room.BuildConfig
import com.soutosss.marvelpoc.data.room.CharacterLocalRoomDataSource
import com.soutosss.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.dsl.module

class DataRoomInitializer : KoinModulesProvider {
    override fun provides(): Module {
        return module {
            single {
                Room.databaseBuilder(
                    get(), AppDatabase::class.java, BuildConfig.DATABASE_NAME
                ).build().charactersLocalDao()
            }
            single {
                CharacterLocalRoomDataSource(
                    get()
                ) as CharacterLocalContract<Character>
            }
        }
    }
}

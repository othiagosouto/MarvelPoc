package dev.thiagosouto.marvelpoc.data.room.koin

import androidx.room.Room
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.room.AppDatabase
import dev.thiagosouto.marvelpoc.data.room.BuildConfig
import dev.thiagosouto.marvelpoc.data.room.CharacterLocalRoomDataSource
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides room module
 */
class DataRoomInitializer : KoinModulesProvider {
    override fun provides(): Module {
        return module {
            single {
                Room.databaseBuilder(
                    get(), AppDatabase::class.java, BuildConfig.DATABASE_NAME
                ).build()
            }
            single { get<AppDatabase>().charactersLocalDao() }
            single {
                CharacterLocalRoomDataSource(
                    get()
                ) as CharacterLocalContract<Character>
            }
        }
    }
}

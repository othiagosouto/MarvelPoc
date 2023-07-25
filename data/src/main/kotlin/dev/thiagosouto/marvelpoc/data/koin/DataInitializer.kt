package dev.thiagosouto.marvelpoc.data.koin

import dev.thiagosouto.marvelpoc.data.CharactersRepositoryImpl
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides data module
 */
class DataInitializer : KoinModulesProvider {
    override fun provides(): Module = module {

        factory {
            CharactersRepositoryImpl(
                get(),
                get(),
                get()
            )
        }

        factory {
            CharactersRepositoryImpl(
                get(),
                get(),
                get()
            ) as FavoritesRepository<Character>
        }

        single { Dispatchers() }
        factory { ComicsMapper() }
    }
}

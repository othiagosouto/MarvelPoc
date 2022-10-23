package dev.thiagosouto.marvelpoc.data.koin

import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides data module
 */
class DataInitializer : KoinModulesProvider {
    override fun provides(): Module = module {

        factory {
            CharactersRepository(
                get(),
                get(),
                get()
            )
        }

        single { Dispatchers() }
        factory { ComicsMapper() }
    }
}

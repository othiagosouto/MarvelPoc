package dev.thiagosouto.marvelpoc.data.koin

import dev.thiagosouto.domain.Mapper
import dev.thiagosouto.domain.MapperList
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.CharacterDetailsService
import dev.thiagosouto.marvelpoc.data.CharactersRepositoryImpl
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.PagingService
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.data.model.view.Character
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides data module
 */
class DataInitializer {
    fun provides(): Module = module {

        single {
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

        factory {
            CharactersRepositoryImpl(
                get(),
                get(),
                get()
            ) as PagingService<Character>
        }

        factory {
            CharactersRepositoryImpl(
                get(),
                get(),
                get()
            ) as CharacterDetailsService<CharacterDetails>
        }

        single { Dispatchers() }
        factory { ComicsMapper() as MapperList<Comics, dev.thiagosouto.marvelpoc.data.model.view.Comics> }
    }
}

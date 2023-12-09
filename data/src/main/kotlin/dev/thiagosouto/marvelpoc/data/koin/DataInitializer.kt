package dev.thiagosouto.marvelpoc.data.koin

import dev.thiagosouto.marvelpoc.data.CharacterDetailsService
import dev.thiagosouto.marvelpoc.data.CharactersRepositoryImpl
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.data.services.DefaultCharacterListService
import dev.thiagosouto.marvelpoc.domain.MapperList
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
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
            )
        }

        factory<FavoritesRepository<Character>> {
            CharactersRepositoryImpl(
                get(),
                get(),
            )
        }

        factory<CharacterDetailsService> {
            CharactersRepositoryImpl(
                get(),
                get(),
            )
        }

        single { Dispatchers() }
        factory<MapperList<Comics, dev.thiagosouto.marvelpoc.domain.model.Comics>>{ ComicsMapper()}
        factory<CharacterListService> {
            DefaultCharacterListService(get())
        }
    }
}

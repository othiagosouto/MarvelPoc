package dev.thiagosouto.marvelpoc.koin

import dev.thiagosouto.marvelpoc.BuildConfig
import dev.thiagosouto.marvelpoc.data.remote.koin.KtorInitializer
import dev.thiagosouto.marvelpoc.features.character.details.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.home.list.CharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Provides app module
 */
internal class AppModulesInitializer {

    fun provides(): Module {
        return module {
            single(named(KtorInitializer.SERVER_URL)) { BuildConfig.BFF_HOST }
            viewModel { FavoritesViewModel(get()) }
            viewModel { CharactersViewModel(get(), get()) }
            viewModel { CharacterDetailsViewModel(get(), get(), get()) }
        }
    }
}

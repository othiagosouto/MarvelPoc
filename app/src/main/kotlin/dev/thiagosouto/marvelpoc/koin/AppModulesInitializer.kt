package dev.thiagosouto.marvelpoc.koin

import dev.thiagosouto.marvelpoc.detail.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.detail.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.home.list.CharactersViewModel
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides app module
 */
class AppModulesInitializer : KoinModulesProvider {

    override fun provides(): Module {
        return module {
            viewModel { FavoritesViewModel(get()) }
            viewModel { CharactersViewModel(get(), get()) }
            viewModel { CharacterDetailsViewModel(get(), get(), get()) }
            factory { DetailsViewStateMapper(get()) }
        }
    }
}

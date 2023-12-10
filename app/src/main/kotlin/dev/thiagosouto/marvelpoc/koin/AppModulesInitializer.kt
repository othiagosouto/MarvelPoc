package dev.thiagosouto.marvelpoc.koin

import dev.thiagosouto.marvelpoc.BuildConfig
import dev.thiagosouto.marvelpoc.data.remote.koin.KtorInitializer
import dev.thiagosouto.marvelpoc.detail.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.detail.DetailsViewState
import dev.thiagosouto.marvelpoc.detail.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.home.list.CharactersViewModel
import dev.thiagosouto.marvelpoc.support.presentation.PresentationMapper
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
            factory<PresentationMapper<DetailsViewStateMapper.Input, DetailsViewState>> {
                DetailsViewStateMapper(
                    get()
                )
            }
        }
    }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

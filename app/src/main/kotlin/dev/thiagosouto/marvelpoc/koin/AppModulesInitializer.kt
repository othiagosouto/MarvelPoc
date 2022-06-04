package dev.thiagosouto.marvelpoc.koin

import dev.thiagosouto.marvelpoc.detail.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.home.HomeViewModel
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
            viewModel { HomeViewModel(get()) }
            viewModel { CharacterDetailsViewModel(get(), get()) }
        }
    }
}
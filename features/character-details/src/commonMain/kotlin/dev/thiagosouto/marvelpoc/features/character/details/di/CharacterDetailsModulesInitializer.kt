package dev.thiagosouto.marvelpoc.features.character.details.di

import dev.thiagosouto.marvelpoc.features.character.details.DetailsViewState
import dev.thiagosouto.marvelpoc.features.character.details.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.features.character.details.domain.DetailsViewStateMapperInput
import dev.thiagosouto.marvelpoc.support.presentation.PresentationMapper
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides app module
 */
class CharacterDetailsModulesInitializer {

    fun provides(): Module {
        return module {
            factory<PresentationMapper<DetailsViewStateMapperInput, DetailsViewState>> {
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

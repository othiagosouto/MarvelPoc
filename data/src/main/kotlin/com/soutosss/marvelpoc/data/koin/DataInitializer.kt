package com.soutosss.marvelpoc.data.koin

import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.mappers.ComicsMapper
import com.soutosss.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.dsl.module

class DataInitializer : KoinModulesProvider {
    override fun provides(): Module = module {

        factory {
            CharactersRepository(
                get(),
                get(),
                get()
            )
        }
        factory { ComicsMapper() }
    }
}

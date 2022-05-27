package com.soutosss.marvelpoc.data.koin

import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.mappers.ComicsMapper
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import org.koin.core.module.Module
import org.koin.dsl.module

class DataInitializer : KoinInitializer {
    override fun createKoinModules(): Module = module {

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

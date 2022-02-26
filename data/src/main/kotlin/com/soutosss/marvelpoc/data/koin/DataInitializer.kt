package com.soutosss.marvelpoc.data.koin

import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import org.koin.core.module.Module
import org.koin.dsl.module

class DataInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(module {

            single {
                CharactersRepository(
                    get(),
                    get(),
                    get()
                )
            }
        })
    }

}

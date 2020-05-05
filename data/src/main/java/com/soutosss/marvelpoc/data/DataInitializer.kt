package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.shared.KoinInitializer
import org.koin.core.module.Module
import org.koin.dsl.module

class DataInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(module {
        })
    }

}
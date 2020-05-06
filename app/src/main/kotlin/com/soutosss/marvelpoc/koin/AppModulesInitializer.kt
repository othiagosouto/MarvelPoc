package com.soutosss.marvelpoc.koin

import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class AppModulesInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(
            module {
                viewModel { HomeViewModel(get()) }
            })
    }
}
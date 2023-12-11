package dev.thiagosouto.marvelpoc.data.local.koin

import dev.thiagosouto.marvelpoc.data.local.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun localModules(): Module = module {
    single { DriverFactory(get()) }
}
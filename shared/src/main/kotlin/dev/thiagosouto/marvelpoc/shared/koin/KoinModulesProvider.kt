package dev.thiagosouto.marvelpoc.shared.koin

import org.koin.core.module.Module

/**
 * Interface to provides koin mudules
 */
interface KoinModulesProvider {

    fun provides(): Module
}

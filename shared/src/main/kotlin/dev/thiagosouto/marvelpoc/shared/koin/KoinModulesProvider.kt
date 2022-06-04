package dev.thiagosouto.marvelpoc.shared.koin

import org.koin.core.module.Module

/**
 * Interface to provides koin module
 */
interface KoinModulesProvider {

    /**
     * provides a koin module
     */
    fun provides(): Module
}

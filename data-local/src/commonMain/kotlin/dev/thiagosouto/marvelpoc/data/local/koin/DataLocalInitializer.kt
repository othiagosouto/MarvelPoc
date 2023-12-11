package dev.thiagosouto.marvelpoc.data.local.koin

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.local.DefaultCharacterLocalContract
import dev.thiagosouto.marvelpoc.data.local.DriverFactory
import dev.thiagosouto.marvelpoc.data.local.MarvelDatabase
import dev.thiagosouto.marvelpoc.data.local.dao.CharacterLocalDAO
import dev.thiagosouto.marvelpoc.data.local.dao.DefaultCharacterLocalDAO
import dev.thiagosouto.marvelpoc.domain.model.Character
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides DataLocal initializer
 */
class DataLocalInitializer {

    fun provides() = module {
        single<MarvelDatabase> { createDatabase(get()) }
        factory<CharacterLocalDAO> { DefaultCharacterLocalDAO(get()) }
        factory<CharacterLocalContract<Character>> { DefaultCharacterLocalContract(get()) }
    } + localModules()

    private fun createDatabase(driverFactory: DriverFactory): MarvelDatabase {
        val driver = driverFactory.createDriver()
        return MarvelDatabase(driver)
    }
}

expect fun localModules(): Module

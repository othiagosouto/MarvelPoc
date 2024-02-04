package dev.thiagosouto.marvelpoc

import android.app.Application
import dev.thiagosouto.marvelpoc.data.koin.DataInitializer
import dev.thiagosouto.marvelpoc.data.local.koin.DataLocalInitializer
import dev.thiagosouto.marvelpoc.data.remote.koin.KtorInitializer
import dev.thiagosouto.marvelpoc.features.character.details.di.CharacterDetailsModulesInitializer
import dev.thiagosouto.marvelpoc.koin.AppModulesInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class MarvelPocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModulesInitializer = AppModulesInitializer()
        val dataModulesInitializer = DataInitializer()
        val characterDetailsModulesInitializer = CharacterDetailsModulesInitializer()
        val ktorInitializer = KtorInitializer()
        val dataLocalInitializer = DataLocalInitializer()
        startKoin {
            androidContext(this@MarvelPocApplication)
            modules(
                appModulesInitializer.provides(),
                dataModulesInitializer.provides(),
                characterDetailsModulesInitializer.provides(),
                ktorInitializer.provides(),
                dataLocalInitializer.provides()[1],
                dataLocalInitializer.provides()[0]
            )
        }
    }
}

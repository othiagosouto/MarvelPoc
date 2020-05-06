package com.soutosss.marvelpoc

import android.app.Application
import com.soutosss.marvelpoc.shared.ModulesInitializer
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MarvelPocApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            loadKoinModules(ModulesInitializer.modules)
        }
    }

}
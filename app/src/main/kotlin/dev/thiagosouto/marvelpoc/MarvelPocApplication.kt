package dev.thiagosouto.marvelpoc

import android.app.Application
import dev.thiagosouto.marvelpoc.data.retrofit.koin.RetrofitInitializer
import dev.thiagosouto.marvelpoc.data.koin.DataInitializer
import dev.thiagosouto.marvelpoc.data.room.koin.DataRoomInitializer
import dev.thiagosouto.marvelpoc.koin.AppModulesInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class MarvelPocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModulesInitializer = AppModulesInitializer()
        val dataModulesInitializer = DataInitializer()
        val dataRoomInitializer = DataRoomInitializer()
        val retrofitInitializer = RetrofitInitializer()
        startKoin {
            androidContext(this@MarvelPocApplication)
            modules(
                appModulesInitializer.provides(),
                dataModulesInitializer.provides(),
                dataRoomInitializer.provides(),
                retrofitInitializer.provides()
            )
        }
    }
}

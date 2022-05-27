package com.soutosss.marvelpoc

import android.app.Application
import com.soutosss.data.retrofit.koin.RetrofitInitializer
import com.soutosss.marvelpoc.data.koin.DataInitializer
import com.soutosss.marvelpoc.data.room.koin.DataRoomInitializer
import com.soutosss.marvelpoc.koin.AppModulesInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelPocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModulesInitializer = AppModulesInitializer()
        val dataModulesInitializer = DataInitializer()
        val dataRoomInitializer = DataRoomInitializer()
        val retrofitInitializer = RetrofitInitializer()
        startKoin {
            androidContext(this@MarvelPocApplication)
            modules(
                appModulesInitializer.createKoinModules(),
                dataModulesInitializer.createKoinModules(),
                dataRoomInitializer.createKoinModules(),
                retrofitInitializer.createKoinModules()
            )
        }
    }
}

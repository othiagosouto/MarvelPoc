package com.soutosss.marvelpoc.data

import android.content.Context
import androidx.room.Room
import com.soutosss.marvelpoc.data.local.AppDatabase
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.data.network.interceptors.ConnectionDetectionInterceptor
import com.soutosss.marvelpoc.data.network.interceptors.MarvelTokenInterceptor
import com.soutosss.marvelpoc.data.network.interceptors.isNetworkNotConnected
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class DataInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(module {
            single { getRetrofitInstance(get()) }
            single {
                Room.databaseBuilder(
                    get(), AppDatabase::class.java, "database-name"
                ).build().charactersHomeDAO()
            }
            single { CharactersRepository(get(), get()) }
        })
    }

    private fun getRetrofitInstance(context: Context): CharactersApi {
        val properties = getApiProperties()
        val httpBuilder = OkHttpClient.Builder()
        httpBuilder.addInterceptor(
            MarvelTokenInterceptor(
                properties.getProperty("publicKey"),
                properties.getProperty("privateKey"),
                System::currentTimeMillis
            )
        )
        httpBuilder.addInterceptor(
            ConnectionDetectionInterceptor(
                context,
                ::isNetworkNotConnected
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/v1/public/")
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersApi::class.java)
    }

    private fun getApiProperties(): Properties {
        val fis = javaClass.classLoader!!.getResource("api.properties").openStream()
        val prop = Properties()
        prop.load(fis)

        return prop
    }
}

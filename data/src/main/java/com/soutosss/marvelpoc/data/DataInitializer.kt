package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.network.CharactersApi
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
            single { getRetrofitInstance() }
            single { CharactersRepository(get()) }
        })
    }

    private fun getRetrofitInstance(): CharactersApi {
        val properties = eta()
        val httpBuilder = OkHttpClient.Builder()
        httpBuilder.addInterceptor(
            MarvelTokenInterceptor(
                properties.getProperty("publicKey"),
                properties.getProperty("privateKey"),
                System::currentTimeMillis
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/v1/public/")
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersApi::class.java)
    }

    private fun eta(): Properties {
        val fis = javaClass.classLoader!!.getResource("api.properties").openStream()
        val prop = Properties()
        prop.load(fis)

        return prop
    }
}

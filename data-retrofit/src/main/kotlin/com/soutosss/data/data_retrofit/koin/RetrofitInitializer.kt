package com.soutosss.data.data_retrofit.koin

import android.content.Context
import com.soutosss.data.data_retrofit.*
import com.soutosss.data.data_retrofit.character.Result
import com.soutosss.data.data_retrofit.interceptors.ConnectionDetectionInterceptor
import com.soutosss.data.data_retrofit.interceptors.MarvelTokenInterceptor
import com.soutosss.data.data_retrofit.interceptors.isNetworkNotConnected
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.character.CharacterDetailsRemoteContract
import com.soutosss.marvelpoc.data.character.CharacterRemoteContract
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RetrofitInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> = listOf(module {
        single { getRetrofitInstance(get()) }
        single { RetrofitCharacterRemote(get()) as CharacterRemoteContract<Result> }
        single { getBffApi()}
        single { RetrofitCharacterDetailsRemote(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
    })

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
            .baseUrl(BuildConfig.SERVICE_HOST)
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersApi::class.java)
    }

    private fun getBffApi(): CharactersBFFApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BFF_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersBFFApi::class.java)
    }

    private fun getApiProperties(): Properties {
        val fis = javaClass.classLoader!!.getResource("api.properties").openStream()
        val prop = Properties()
        prop.load(fis)

        return prop
    }
}
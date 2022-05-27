package com.soutosss.data.data_retrofit.koin

import android.content.Context
import com.soutosss.data.data_retrofit.BuildConfig
import com.soutosss.data.data_retrofit.CharactersBFFApi
import com.soutosss.data.data_retrofit.RetrofitCharacterDetailsRemote
import com.soutosss.data.data_retrofit.RetrofitCharacterRemote
import com.soutosss.data.data_retrofit.character.Result
import com.soutosss.data.data_retrofit.interceptors.ConnectionDetectionInterceptor
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

class RetrofitInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> = listOf(module {
        single { RetrofitCharacterRemote(get()) as CharacterRemoteContract<Result> }
        single { getRetrofitInstance(get())}
        single { RetrofitCharacterDetailsRemote(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
    })

    private fun getRetrofitInstance(context: Context): CharactersBFFApi {
        val httpBuilder = OkHttpClient.Builder()

        httpBuilder.addInterceptor(
            ConnectionDetectionInterceptor(
                context,
                ::isNetworkNotConnected
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BFF_HOST)
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersBFFApi::class.java)
    }
}

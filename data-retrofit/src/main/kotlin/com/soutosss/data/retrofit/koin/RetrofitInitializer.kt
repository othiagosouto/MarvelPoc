package com.soutosss.data.retrofit.koin

import android.content.Context
import com.soutosss.data.retrofit.BuildConfig
import com.soutosss.data.retrofit.CharactersBFFApi
import com.soutosss.data.retrofit.RetrofitCharacterDetailsRemote
import com.soutosss.data.retrofit.RetrofitCharacterRemote
import com.soutosss.data.retrofit.character.Result
import com.soutosss.data.retrofit.interceptors.ConnectionDetectionInterceptor
import com.soutosss.data.retrofit.interceptors.isNetworkNotConnected
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.character.CharacterDetailsRemoteContract
import com.soutosss.marvelpoc.data.character.CharacterRemoteContract
import com.soutosss.marvelpoc.shared.koin.KoinInitializer
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer : KoinInitializer {
    override fun createKoinModules(): Module =
        module {
            single(named(SERVER_URL)) { BuildConfig.BFF_HOST }
            single { RetrofitCharacterRemote(get()) as CharacterRemoteContract<Result> }
            single { getRetrofitInstance(get(), get(named(SERVER_URL))) }
            single { RetrofitCharacterDetailsRemote(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
        }

    private fun getRetrofitInstance(context: Context, bffHost: String): CharactersBFFApi {
        val httpBuilder = OkHttpClient.Builder()

        httpBuilder.addInterceptor(
            ConnectionDetectionInterceptor(
                context,
                ::isNetworkNotConnected
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(bffHost)
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersBFFApi::class.java)
    }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

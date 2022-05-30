package dev.thiagosouto.data.retrofit.koin

import android.content.Context
import dev.thiagosouto.data.retrofit.BuildConfig
import dev.thiagosouto.data.retrofit.CharactersBFFApi
import dev.thiagosouto.data.retrofit.RetrofitCharacterDetailsRemote
import dev.thiagosouto.data.retrofit.RetrofitCharacterRemote
import dev.thiagosouto.data.retrofit.character.Result
import dev.thiagosouto.data.retrofit.interceptors.ConnectionDetectionInterceptor
import dev.thiagosouto.data.retrofit.interceptors.isNetworkNotConnected
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer : KoinModulesProvider {
    override fun provides(): Module =
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

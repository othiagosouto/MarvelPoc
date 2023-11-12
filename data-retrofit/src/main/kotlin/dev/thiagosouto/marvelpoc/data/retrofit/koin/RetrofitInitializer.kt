package dev.thiagosouto.marvelpoc.data.retrofit.koin

import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.retrofit.BuildConfig
import dev.thiagosouto.marvelpoc.data.retrofit.CharactersBFFApi
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterDetailsRemote
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterRemote
import dev.thiagosouto.marvelpoc.data.retrofit.character.Result
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides retrofit module
 */
class RetrofitInitializer : KoinModulesProvider {
    override fun provides(): Module =
        module {
            single(named(SERVER_URL)) { BuildConfig.BFF_HOST }
            single { RetrofitCharacterRemote(get()) as CharacterRemoteContract<Result> }
            single { getRetrofitInstance(get(named(SERVER_URL))) }
            single { RetrofitCharacterDetailsRemote(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
        }

    private fun getRetrofitInstance(bffHost: String): CharactersBFFApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(bffHost)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CharactersBFFApi::class.java)
    }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

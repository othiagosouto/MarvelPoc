package dev.thiagosouto.marvelpoc.data.retrofit.koin

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.thiagosouto.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.retrofit.BuildConfig
import dev.thiagosouto.marvelpoc.data.retrofit.CharactersBFFApi
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterDetailsRemote
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterRemote
import dev.thiagosouto.marvelpoc.data.retrofit.character.Result
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

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
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(CharactersBFFApi::class.java)
    }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

package dev.thiagosouto.marvelpoc.data.retrofit.koin

import dev.thiagosouto.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.retrofit.BuildConfig
import dev.thiagosouto.marvelpoc.data.retrofit.CharactersBFFApi
import dev.thiagosouto.marvelpoc.data.retrofit.DefaultCharactersBFFApi
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterDetailsRemote
import dev.thiagosouto.marvelpoc.data.retrofit.RetrofitCharacterRemote
import dev.thiagosouto.marvelpoc.data.retrofit.character.Result
import dev.thiagosouto.marvelpoc.shared.koin.KoinModulesProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Provides retrofit module
 */
class KtorInitializer : KoinModulesProvider {
    override fun provides(): Module =
        module {
            single(named(SERVER_URL)) { BuildConfig.BFF_HOST }
            single {
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                }
            }
            single {
                DefaultCharactersBFFApi(get(), BuildConfig.BFF_HOST) as CharactersBFFApi
            }
            single { RetrofitCharacterDetailsRemote(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
            single { RetrofitCharacterRemote(get()) as CharacterRemoteContract<Result> }
        }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

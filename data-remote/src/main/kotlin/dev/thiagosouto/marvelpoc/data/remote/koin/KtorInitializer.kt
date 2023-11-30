package dev.thiagosouto.marvelpoc.data.remote.koin

import dev.thiagosouto.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.remote.BuildConfig
import dev.thiagosouto.marvelpoc.data.remote.CharactersBFFApi
import dev.thiagosouto.marvelpoc.data.remote.DefaultCharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.remote.DefaultCharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.remote.DefaultCharactersBFFApi
import dev.thiagosouto.marvelpoc.data.remote.character.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Provides Ktor module
 */
class KtorInitializer {
    fun provides(): Module =
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
                DefaultCharactersBFFApi(get(), get(named(SERVER_URL))) as CharactersBFFApi
            }
            single { DefaultCharacterDetailsRemoteContract(get()) as CharacterDetailsRemoteContract<CharacterDetails> }
            single { DefaultCharacterRemoteContract(get()) as CharacterRemoteContract<Result> }
        }

    companion object {
        const val SERVER_URL = "SERVER_URL"
    }
}

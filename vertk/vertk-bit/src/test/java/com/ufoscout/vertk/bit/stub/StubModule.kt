package com.ufoscout.vertk.bit.stub

import com.ufoscout.vertk.bit.VertkBitModule
import com.ufoscout.vertk.bit.deployBitVerticleAwait
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.koin.core.Koin
import java.util.*

class StubModule(val deploymentOptions: DeploymentOptions = DeploymentOptions()): VertkBitModule {

    companion object {
        val RANDOM_NAME= UUID.randomUUID().toString()
    }

    override fun module() = org.koin.dsl.module {
        single { RANDOM_NAME }
        single(createdAtStart = true) { VertxKComponentImpl(get()) }
        factory { VertxKVerticleImpl(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployBitVerticleAwait<VertxKVerticleImpl>(deploymentOptions)
    }

}
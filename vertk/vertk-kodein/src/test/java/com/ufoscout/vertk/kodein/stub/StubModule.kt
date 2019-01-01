package com.ufoscout.vertk.kodein.stub

import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.deployKodeinVerticleAwait
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.koin.core.Koin
import java.util.*

class StubModule(val deploymentOptions: DeploymentOptions = DeploymentOptions()): VertkKodeinModule {

    companion object {
        val RANDOM_NAME= UUID.randomUUID().toString()
    }

    override fun module() = org.koin.dsl.module {
        single { RANDOM_NAME }
        single(createdAtStart = true) { VertxKComponentImpl(get()) }
        factory { VertxKVerticleImpl(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployKodeinVerticleAwait<VertxKVerticleImpl>(deploymentOptions)
    }

}
package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.deployKodeinVerticleAwait
import io.vertx.core.Vertx
import org.koin.core.Koin

class RouterTestModule: VertkKodeinModule {

    override fun module() = org.koin.dsl.module {
        factory { TestWebController(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployKodeinVerticleAwait<TestWebController>()
    }

}
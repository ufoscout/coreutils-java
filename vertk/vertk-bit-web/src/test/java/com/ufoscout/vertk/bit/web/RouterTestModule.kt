package com.ufoscout.vertk.bit.web

import com.ufoscout.vertk.bit.VertkBitModule
import com.ufoscout.vertk.bit.deployBitVerticleAwait
import io.vertx.core.Vertx
import org.koin.core.Koin

class RouterTestModule: VertkBitModule {

    override fun module() = org.koin.dsl.module {
        factory { TestWebController(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployBitVerticleAwait<TestWebController>()
    }

}
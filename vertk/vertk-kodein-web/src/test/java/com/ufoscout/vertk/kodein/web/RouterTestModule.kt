package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.deployKodeinVerticleAwait
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class RouterTestModule: VertkKodeinModule {

    override fun module() = Kodein.Module {}

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.deployKodeinVerticleAwait<TestWebController>()
    }

}
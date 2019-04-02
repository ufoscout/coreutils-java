package com.ufoscout.vertk.bit

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.bit.web.AuthenticationController
import io.vertx.core.Vertx
import org.koin.core.Koin

class AuthTestModule: VertkBitModule {

    override fun module() = org.koin.dsl.module{
        single<RolesProvider> { InMemoryRolesProvider() }
        factory { AuthenticationController(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployBitVerticleAwait<AuthenticationController>()
    }

}
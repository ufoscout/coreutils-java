package com.ufoscout.vertk.kodein

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.kodein.web.AuthenticationController
import io.vertx.core.Vertx
import org.koin.core.Koin

class AuthTestModule: VertkKodeinModule {

    override fun module() = org.koin.dsl.module{
        single<RolesProvider> { InMemoryRolesProvider() }
        factory { AuthenticationController(get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        vertx.deployKodeinVerticleAwait<AuthenticationController>()
    }

}
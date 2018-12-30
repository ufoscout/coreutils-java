package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.auth.AuthServiceImpl
import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.coreutils.jwt.JwtServiceJJWT
import com.ufoscout.coreutils.jwt.kotlin.CoreJsonProvider
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance

class AuthModule(val jwtConfig: JwtConfig): VertkKodeinModule {

    override fun module(vertx: Vertx) = Kodein.Module {
        bind<AuthService>() with eagerSingleton {
            AuthServiceImpl(instance())
        }
        bind<AuthContextService>() with eagerSingleton {
            AuthContextServiceImpl(instance(), instance(), instance())
        }
        bind<JwtService>() with eagerSingleton {
            JwtService(JwtServiceJJWT(jwtConfig, CoreJsonProvider(instance())))
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
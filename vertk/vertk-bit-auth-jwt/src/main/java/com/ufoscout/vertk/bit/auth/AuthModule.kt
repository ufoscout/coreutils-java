package com.ufoscout.vertk.bit.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.auth.AuthServiceImpl
import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.coreutils.jwt.JwtServiceJJWT
import com.ufoscout.coreutils.jwt.kotlin.CoreJsonProvider
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.bit.VertkBitModule
import io.vertx.core.Vertx
import org.koin.core.Koin

class AuthModule(val jwtConfig: JwtConfig): VertkBitModule {

    override fun module() = org.koin.dsl.module {
        single<AuthService> {
            AuthServiceImpl(get())
        }
        single<AuthContextService> {
            AuthContextServiceImpl(get(), get(), get())
        }
        single<JwtService> {
            JwtService(JwtServiceJJWT(jwtConfig, CoreJsonProvider(get())))
        }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
    }

}
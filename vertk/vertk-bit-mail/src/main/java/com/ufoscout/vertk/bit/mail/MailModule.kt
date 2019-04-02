package com.ufoscout.vertk.bit.mail

import com.ufoscout.vertk.bit.VertkBitModule
import io.vertx.core.Vertx
import org.koin.core.Koin


class MailModule(private val mailConfig: MailConfig): VertkBitModule {

    override fun module() = org.koin.dsl.module {
        single<MailClient> { MailClientFactory.build(mailConfig, get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
    }

}
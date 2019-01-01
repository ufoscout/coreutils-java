package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import org.koin.core.Koin


class MailModule(private val mailConfig: MailConfig): VertkKodeinModule {

    override fun module() = org.koin.dsl.module {
        single<MailClient> { MailClientFactory.build(mailConfig, get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
    }

}
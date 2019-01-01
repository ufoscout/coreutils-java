package com.ufoscout.vertk

import com.ufoscout.vertk.kodein.VertkKodein
import com.ufoscout.vertk.kodein.mail.MailClientFactory
import com.ufoscout.vertk.kodein.mail.MailConfig
import com.ufoscout.vertk.kodein.mail.MailModule
import io.vertx.core.Vertx
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.koin.core.Koin
import org.testcontainers.containers.GenericContainer

abstract class BaseIT : BaseTest() {

    companion object {

        private var vertk: Vertx? = null
        private var koin: Koin? = null
        private var mailConfig: MailConfig? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            val mh = MailhogContainer("mailhog/mailhog:latest")
                    .withExposedPorts(1025)
            mh!!.start()

            mailConfig = MailConfig(
                    clientType = MailClientFactory.VERTX,
                    config = io.vertx.ext.mail.MailConfig()
                            .setHostname(mh!!.getContainerIpAddress().toString())
                            .setPort(mh!!.getMappedPort(1025))
            )

            vertk = Vertx.vertx()

            koin = VertkKodein.start(
                    vertk!!,
                    MailModule(mailConfig!!)
            )

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            vertk!!.close()
        }

    }

    protected fun vertk(): Vertx = vertk!!
    protected fun mailConfig() = mailConfig!!
    protected fun koin(): Koin = koin!!

}

class MailhogContainer(val dockerImage: String): GenericContainer<MailhogContainer>(dockerImage) {}
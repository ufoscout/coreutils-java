package com.ufoscout.vertk.bit.mail

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult
import io.vertx.kotlin.ext.mail.sendMailAwait

class VertxMailClient(private val config: io.vertx.ext.mail.MailConfig, private val vertx: Vertx): com.ufoscout.vertk.bit.mail.MailClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mailClient: MailClient

    init {
        logger.info("Configure MailClient for server ${config.hostname}:${config.port} ")
        mailClient = MailClient.createNonShared(vertx, config)
    }

    override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
        return mailClient.sendMailAwait(mailMessage)
    }

}
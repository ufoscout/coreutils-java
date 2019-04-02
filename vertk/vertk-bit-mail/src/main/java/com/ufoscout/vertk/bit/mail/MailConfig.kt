package com.ufoscout.vertk.bit.mail

class MailConfig(val clientType: String, val config: io.vertx.ext.mail.MailConfig = io.vertx.ext.mail.MailConfig())
{}
package com.ufoscout.vertk.kodein.web

import io.vertx.ext.web.Router

interface RouterService {

    fun router(): Router

    suspend fun start()
}
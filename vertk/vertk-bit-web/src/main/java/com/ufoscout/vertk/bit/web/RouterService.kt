package com.ufoscout.vertk.bit.web

import io.vertx.ext.web.Router

interface RouterService {

    fun router(): Router

    suspend fun start()
}
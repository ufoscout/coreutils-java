package com.ufoscout.vertk.web.verticle

import com.ufoscout.vertk.web.*
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*

class HttpVerticle: CoroutineVerticle() {

    companion object {
        val path = "/" + UUID.randomUUID().toString()
    }

    override suspend fun start() {

        val router = BaseTest.router

        router.deleteRestAwait(path) {
            ResponseDTO(it.request().method().toString())
        }


        router.getRestAwait(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.optionsRestAwait(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.patchRestAwait<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.postRestAwait<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.putRestAwait<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }
    }

}
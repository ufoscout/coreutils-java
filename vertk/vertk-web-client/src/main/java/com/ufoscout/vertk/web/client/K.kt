package com.ufoscout.vertk.web.client

import com.fasterxml.jackson.module.kotlin.readValue
import io.vertx.core.json.Json
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse

fun <T> HttpRequest<T>.putHeaders(vararg headers: Pair<String, String>): HttpRequest<T> {
    headers.forEach { this.putHeader(it.first, it.second) }
    return this
}

inline fun <reified T> HttpResponse<out Any>.bodyAsJson(): T {
    return Json.mapper.readValue(this.bodyAsString())
}

package com.ufoscout.vertk.web.client

import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse

fun <T : Any> HttpRequest<T>.putHeaders(vararg headers: Pair<String, String>): HttpRequest<T> {
    headers.forEach { this.putHeader(it.first, it.second) }
    return this
}

inline fun <reified T : Any> HttpResponse<out Any>.bodyAsJson(): T {
        return this.bodyAsJson(T::class.java)
}

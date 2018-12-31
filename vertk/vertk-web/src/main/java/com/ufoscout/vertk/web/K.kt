package com.ufoscout.vertk.web

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun HttpServerResponse.endWithJson(obj: Any) {
    this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(obj))
}

fun Route.handlerAwait(handler: suspend (rc: RoutingContext) -> Any) {
    this.handler {
        GlobalScope.launch (Vertx.currentContext().dispatcher()) {
            try {
                handler(it)
            } catch (e: Exception) {
                it.fail(e)
            }
        }
    }
}

fun Router.deleteRestAwait(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.delete(path), handler)
}

fun Router.deleteWithRegexRestAwait(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.deleteWithRegex(regex), handler)
}

fun Router.getRestAwait(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.get(path), handler)
}

fun Router.getWithRegexRestAwait(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.getWithRegex(regex), handler)
}

fun Router.headWithRegexRestAwait(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.headWithRegex(regex), handler)
}

fun Router.optionsRestAwait(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.options(path), handler)
}

fun Router.optionsWithRegexRestAwait(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    restAwait(this.optionsWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.patchRestAwait(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.patch(path), handler)
}

inline fun <reified I : Any> Router.patchWithRegexRestAwait(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.patchWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.postRestAwait(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.post(path), handler)
}

inline fun <reified I : Any> Router.postWithRegexRestAwait(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.postWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.putRestAwait(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.put(path), handler)
}

inline fun <reified I : Any> Router.putWithRegexRestAwait(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBodyAwait<I>(this.putWithRegex(regex), handler)
}

fun restAwait(route: Route, handler: suspend (rc: RoutingContext) -> Any) {
    route
            .produces("application/json")
            .handlerAwait {
                        val result = handler(it)
                        var resultJson = if (result is String) {
                            result
                        } else {
                            Json.encode(result)
                        }
                        it.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson)
            }
}

inline fun <reified I : Any> restWithBodyAwait(route: Route, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    route
            .consumes("application/json")
            .produces("application/json")
            .handlerAwait { rc ->
                        val bodyBuffer = awaitEvent<Buffer> {
                            rc.request().bodyHandler(it)
                        }
                        val body = bodyBuffer.toJsonObject().mapTo(I::class.java)
                        val result = handler(rc, body)
                        var resultJson = if (result is String) {
                            result
                        } else {
                            Json.encode(result)
                        }
                        rc.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson)
            }
}



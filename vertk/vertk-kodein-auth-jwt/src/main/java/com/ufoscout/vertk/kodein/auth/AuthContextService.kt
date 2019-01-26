package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.jwt.Token
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService {

    fun tokenFrom(routingContext: RoutingContext): String? {
        return tokenFrom(routingContext.request())
    }

    fun tokenFrom(request: HttpServerRequest): String?

    fun from(routingContext: RoutingContext): AuthContext {
        return from(routingContext.request())
    }

    fun from(request: HttpServerRequest): AuthContext

    fun from(auth: Auth): AuthContext

    fun generateToken(auth: Auth): Token

}
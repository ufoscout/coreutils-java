package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.*
import com.ufoscout.coreutils.jwt.TokenExpiredException
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.coreutils.jwt.Token
import com.ufoscout.vertk.kodein.web.WebException
import com.ufoscout.vertk.kodein.web.WebExceptionService
import com.ufoscout.vertk.kodein.web.registerTransformer
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpServerRequest

open class AuthContextServiceImpl(
        val webExceptionService: WebExceptionService,
        val authService: AuthService,
        val jwtService: JwtService): AuthContextService {

    override fun tokenFrom(request: HttpServerRequest): String? {
        val header = request.getHeader(AuthContants.JWT_TOKEN_HEADER);
        if (header!=null && header.startsWith(AuthContants.JWT_TOKEN_HEADER_SUFFIX)) {
            return header.substring(AuthContants.JWT_TOKEN_HEADER_SUFFIX.length)
        }
        return null
    }

    init {

        webExceptionService.registerTransformer<UnauthenticatedException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "NotAuthenticated") }
        webExceptionService.registerTransformer<BadCredentialsException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "BadCredentials") }
        webExceptionService.registerTransformer<UnauthorizedException> { ex -> WebException(cause = ex, code = HttpResponseStatus.FORBIDDEN.code(), message = "AccessDenied") }
        webExceptionService.registerTransformer<TokenExpiredException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "TokenExpired") }

        authService.start()
    }

    override fun from(auth: Auth): AuthContext {
        return authService.auth(auth)
    }

    override fun from(request: HttpServerRequest): AuthContext {
        val token = tokenFrom(request)
        if (token!=null) {
            val user: Auth = jwtService.parse(token, Auth::class)
            return from(user)
        }
        return from(Auth())
    }

    override fun generateToken(auth: Auth): Token {
        return jwtService.generate(auth.username, auth)
    }

}
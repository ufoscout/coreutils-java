package com.ufoscout.vertk.bit

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.BaseIT
import com.ufoscout.vertk.bit.auth.AuthContants
import com.ufoscout.vertk.bit.web.AuthenticationController
import com.ufoscout.vertk.bit.web.ErrorDetails
import com.ufoscout.vertk.bit.web.LoginDto
import com.ufoscout.vertk.bit.web.LoginResponseDto
import com.ufoscout.vertk.web.client.bodyAsJson
import com.ufoscout.vertk.web.client.putHeaders
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendAwait
import io.vertx.kotlin.ext.web.client.sendJsonAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


class AuthenticationControllerIT : BaseIT() {

    val client = WebClient.create(vertk)
    val jwt: JwtService = koin().get()
    val authService: AuthService = koin().get()

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")
        val response = client.post(port(), "localhost", AuthenticationController.BASE_AUTH_API + "/login").sendJsonAwait(loginDto)

        val body = response.bodyAsJson<LoginResponseDto>()

        assertEquals(200, response.statusCode())
        logger.info("token is ${body.token}")

    }

    @Test
    fun shouldGetUnauthorizedWithAnonymousAuth() = runBlocking<Unit> {
        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/authenticated").sendAwait()

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode())

        val body = response.bodyAsJson<ErrorDetails>()
        assertEquals("NotAuthenticated", body.message)
    }

    @Test

    fun shouldGetUnauthorizedWithAnonymousAuthOnProtectedUri() = runBlocking<Unit> {
        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected").sendAwait()

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode())

        val body = response.bodyAsJson<ErrorDetails>()
        assertEquals("NotAuthenticated", body.message)
    }

    @Test
    fun shouldSuccessfulAccessAuthenticatedApiWithToken() = runBlocking<Unit> {

        val sentAuthContext = Auth(0, UUID.randomUUID().toString(), arrayOf("ADMIN", "OTHER"))

        val token = jwt.generate(sentAuthContext).token

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/authenticated")
                .putHeaders(headers)
                .sendAwait()

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode())

        val receivedAuthContext = response.bodyAsJson<Auth>()
        assertNotNull(receivedAuthContext)
        assertEquals(sentAuthContext.username, receivedAuthContext.username)
    }

    @Test
    fun shouldAccessPublicUriWithAnonymousAuth() = runBlocking<Unit> {

        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/public").sendAwait()

        val userContext = response.bodyAsJson<Auth>()
        assertNotNull(userContext)
        assertTrue(userContext!!.roles.size === 0)
        assertTrue(userContext!!.username.isEmpty())
    }


    @Test
    fun shouldSuccessfulLoginWithValidCredentials() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")

        val response = client.post(
                port(),
                "localhost",
                AuthenticationController.BASE_AUTH_API + "/login").
                sendJsonAwait(loginDto)


        val responseDto = response.bodyAsJson<LoginResponseDto>()
        assertNotNull(responseDto)
        assertNotNull(responseDto!!.token)
        val userContext = jwt.parse(responseDto.token.token, Auth::class)
        assertEquals("user", userContext.username)
        assertEquals(1, arrayOf(userContext.roles).size)
        assertEquals("USER", authService.getByName(*userContext.roles)[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun shouldFailLoginWithWrongCredentials() = runBlocking {

        val loginDto = LoginDto("user", UUID.randomUUID().toString())

        val response = client.post(
                port(),
                "localhost",
                AuthenticationController.BASE_AUTH_API + "/login").
                sendJsonAwait(loginDto)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode())

        val body = response.bodyAsJson<ErrorDetails>()
        assertEquals("BadCredentials", body.message)
    }


    @Test
    fun shouldNotAccessProtectedApiWithoutAdminRole() = runBlocking {

        val sentAuthContext = Auth(0, UUID.randomUUID().toString())

        val token = jwt.generate(sentAuthContext).token

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected").
                putHeaders(headers).
                sendAwait()

        assertEquals(HttpResponseStatus.FORBIDDEN.code(), response.statusCode())

        val body = response.bodyAsJson<ErrorDetails>()
        assertEquals("AccessDenied", body.message)

    }

    @Test
    fun shouldSuccessfulAccessProtectedApiWithAdminRole() = runBlocking {

        val sentAuthContext = Auth(0, UUID.randomUUID().toString(), arrayOf("ADMIN"))

        val token = jwt.generate(sentAuthContext).token

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected").
                putHeaders(headers)
                .sendAwait()

        val receivedAuthContext = response.bodyAsJson<Auth>()
        assertNotNull(receivedAuthContext)
        assertEquals(sentAuthContext.username, receivedAuthContext!!.username)
    }

    @Test
    fun shouldGetTokenExpiredExceptionIfTokenNotValid() = runBlocking {

        val sentAuthContext = Auth(0, UUID.randomUUID().toString())

        val token = jwt.generate("", sentAuthContext, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() - 1000)).token

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.get(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected").
                putHeaders(headers)
                .sendAwait()

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode())

        val body = response.bodyAsJson<ErrorDetails>()
        assertEquals("TokenExpired", body.message)

    }

}

package com.ufoscout.coreutils.jwt.kotlin

import com.ufoscout.coreutils.jwt.Token
import com.ufoscout.coreutils.jwt.TokenExpiredException
import java.util.*
import kotlin.reflect.KClass

/**
 * Interface to parse and generate JWTs
 *
 * @author Francesco Cina'
 */
class JwtService(val jwtService: com.ufoscout.coreutils.jwt.JwtService) {

    /**
     * Generates a JWT from the payload
     * @param payload the JWT payload
     * @return
     */
    fun generate(payload: Any): Token {
        return jwtService.generate(payload)
    }

    /**
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @return
     */
    fun generate(subject: String, payload: Any): Token {
        return jwtService.generate(subject, payload)
    }

    /**
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @param createdDate the creation Date
     * @param expirationDate the expiration Date
     * @return
     */
    fun generate(subject: String, payload: Any, createdDate: Date, expirationDate: Date): Token {
        return jwtService.generate(subject, payload, createdDate, expirationDate)
    }

    /**
     * Parses a JWT and return the contained bean.
     * It throws [TokenExpiredException] if the token has expired.
     *
     * @param jwt
     * @param payloadClass
     * @return
     */
    @Throws(TokenExpiredException::class)
    fun <T : Any> parse(jwt: String, payloadClass: KClass<T>): T {
        return jwtService.parse(jwt, payloadClass.java)
    }

    /**
     * Parses a JWT and return the contained bean.
     * It throws [TokenExpiredException] if the token has expired.
     *
     * @param jwt
     * @return
     */
    @Throws(TokenExpiredException::class)
    inline fun <reified T : Any> parse(jwt: String) = parse(jwt, T::class)
}


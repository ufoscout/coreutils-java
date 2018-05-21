package com.ufoscout.coreutils.jwt.kotlin

import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.coreutils.jwt.JsonProvider

class CoreJsonProvider(private val jjService: JsonSerializerService) : JsonProvider {

    override fun <T : Any> toJson(payload: T): String {
        return jjService.toJson(payload)
    }

    override fun <T : Any> fromJson(payloadClass: Class<T>, json: String): T {
        return jjService.fromJson(payloadClass.kotlin, json)
    }
}

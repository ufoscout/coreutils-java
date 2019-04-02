package com.ufoscout.vertk.bit.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ufoscout.coreutils.json.JacksonJsonSerializerService
import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.vertk.bit.VertkBitModule
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import org.koin.core.Koin

class JsonModule: VertkBitModule {

    override fun module() = org.koin.dsl.module {
        initMapper(Json.mapper)
        initMapper(Json.prettyMapper)

        single { JsonSerializerService(JacksonJsonSerializerService(Json.mapper)) }

    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {}

    private fun initMapper(mapper: ObjectMapper) {
        mapper.registerModule(KotlinModule())
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(Jdk8Module())
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    }
}

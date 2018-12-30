package com.ufoscout.vertk.kodein.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ufoscout.coreutils.json.JacksonJsonSerializerService
import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton

class JsonModule: VertkKodeinModule {

    override fun module(vertx: Vertx) = Kodein.Module {
        initMapper(Json.mapper)
        initMapper(Json.prettyMapper)

        bind<JsonSerializerService>() with eagerSingleton {
            JsonSerializerService(JacksonJsonSerializerService(Json.mapper))
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {}

    private fun initMapper(mapper: ObjectMapper) {
        mapper.registerModule(KotlinModule())
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(Jdk8Module())
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    }
}

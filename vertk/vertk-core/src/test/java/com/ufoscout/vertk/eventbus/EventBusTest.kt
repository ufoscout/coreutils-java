package com.ufoscout.vertk.eventbus

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ufoscout.vertk.BaseTest
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class EventBusTest: BaseTest() {

    @Test
    fun shouldSendAndAwait() = runBlocking<Unit> {

        val address = UUID.randomUUID().toString()
        val request = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        vertx.eventBus().consumer(address) { message: Message<String> ->
            assertEquals(request, message.body())
            message.reply(response)
        }

        val message = vertx.eventBus().awaitSend<String>(address, request)
        assertEquals(response, message.body())

    }

    @Test
    fun shouldPublishAndAwait() = runBlocking<Unit> {

        val address = UUID.randomUUID().toString()
        val request = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        vertx.eventBus().awaitConsumer(address) { message: String ->
            assertEquals(request, message)
            response
        }

        val message = vertx.eventBus().awaitSend<String>(address, request)
        assertEquals(response, message.body())

    }

    @Test
    fun shouldPublishCustomMessageWithJsonCodec() = runBlocking<Unit> {

        Json.mapper.registerModule(KotlinModule())

        vertx.eventBus().registerJsonCodec<MyClass>()

        val address = UUID.randomUUID().toString()
        val request = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        vertx.eventBus().awaitConsumer(address) { message: MyClass ->
            assertEquals(request, message.value)
            MyClass(value = response)
        }

        val message = vertx.eventBus().awaitSend<MyClass>(address, MyClass(value = request))
        assertEquals(response, message.body().value)

    }

    data class MyClass(val value: String)

}
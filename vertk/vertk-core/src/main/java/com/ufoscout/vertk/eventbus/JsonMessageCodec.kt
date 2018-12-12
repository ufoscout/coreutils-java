package com.ufoscout.vertk.eventbus

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.Json


class JsonMessageCodec<T>(private val clazz: Class<T>): MessageCodec<T, T> {

    companion object {
        inline fun <reified T> new(): JsonMessageCodec<T> {
            return JsonMessageCodec(clazz = T::class.java)
        }
    }

    override fun encodeToWire(buffer: Buffer, customMessage: T) {
        buffer.appendString(Json.encode(customMessage))
    }

    override fun decodeFromWire(position: Int, buffer: Buffer): T {

        // Length of JSON
        val length = buffer.getInt(position)

        // Get JSON string by it`s length
        val end_position = position + length
        val jsonStr = buffer.getString(position , end_position)

        // We can finally create custom message object
        return Json.decodeValue(jsonStr, clazz)
    }

    override fun transform(customMessage: T): T {
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return customMessage
    }

    override fun name(): String {
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.javaClass.simpleName
    }

    override fun systemCodecID(): Byte {
        // Always -1
        return -1
    }

}
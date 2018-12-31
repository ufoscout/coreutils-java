package com.ufoscout.vertk.eventbus

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.eventbus.ReplyException
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * To return a custom message failure code, the callback can throw a ReplyException
 */
inline fun <T> EventBus.consumerAwait(address: String, noinline handler: suspend (message: T) -> Any): MessageConsumer<T> {
    return this.consumer(address) { message: Message<T> ->
        GlobalScope.launch (Vertx.currentContext().dispatcher()) {
            try {
                message.reply(handler(message.body()))
            } catch (e: ReplyException) {
                message.fail(e.failureCode(), e.message)
            } catch (e: Exception) {
                message.fail(0, e.message)
            }
        }
    }
}

/**
 * Register the JsonMessageCodec as default message codec for the class specified.
 *
 * @param clazz  the class for which to use this codec
 */
inline fun <reified T> EventBus.registerJsonCodec() {
    this.registerDefaultCodec(T::class.java, JsonMessageCodec.new<T>())
}

suspend fun EventBus.closeAwait() {
    awaitResult<Void> { this.close(it) }
}

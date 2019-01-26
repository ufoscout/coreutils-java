package com.ufoscout.vertk

import com.ufoscout.vertk.eventbus.Addr
import com.ufoscout.vertk.eventbus.EventBusWithGroups
import io.vertx.core.*
import io.vertx.kotlin.core.executeBlockingAwait
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Executes a suspendable function in the current Vertx context without blocking current thread.
 * This uses the kotlin coroutines "launch()" using the current Vertx dispatcher.
 */
fun Vertx.launch(action: suspend () -> Unit) {
    GlobalScope.launch (this.orCreateContext.dispatcher()) {
        action()
    }
}

/**
 * Executes a suspendable function in the current Vertx context.
 * This uses the kotlin coroutines "runBlocking()" using the current Vertx dispatcher.
 *
 * WARN: this methods BLOCKS the current thread!
 */
fun <T> Vertx.runBlocking(action: suspend () -> T): T {
    return kotlinx.coroutines.runBlocking(this.orCreateContext.dispatcher()) {
        action()
    }
}

suspend fun <R> Vertx.executeBlockingAwait(action: () -> R): R {
    val handler: Handler<Future<R>> = Handler {
        try {
            val result = action()
            it.complete(result)
        } catch (e: Throwable) {
            it.fail(e)
        }
    }
    return this.executeBlockingAwait(handler)!!
}

suspend fun <R> Vertx.executeBlockingAwait(action: () -> R, ordered: Boolean): R {
        val handler: Handler<Future<R>> = Handler {
            try {
                val result = action()
                it.complete(result)
            } catch (e: Throwable) {
                it.fail(e)
            }
        }
        return this.executeBlockingAwait(handler, ordered)!!
}

suspend fun Vertx.deployVerticleAwait(verticle: Verticle, deploymentOptions: DeploymentOptions) {
    awaitResult<String> {
        this.deployVerticle(verticle, it)
    }
}

suspend fun Vertx.deployVerticleAwait(verticle: Verticle) {
    awaitResult<String> {
        this.deployVerticle(verticle, it)
    }
}

suspend fun Vertx.deployVerticleAwait(supplier: () -> Verticle, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(java.util.function.Supplier { supplier() }, deploymentOptions, it)
    }
}


inline suspend fun <reified T : Verticle> Vertx.deployVerticleAwait(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(T::class.java, deploymentOptions, it)
    }
}

/**
 * Returns a new EventBusWithGroups
 */
fun <T> Vertx.eventBusWithGroups(address: Addr<T>): EventBusWithGroups<T> {
    return EventBusWithGroups(vertx = this, address = address)
}

package com.ufoscout.vertk.shared

import io.vertx.core.shareddata.AsyncMap
import io.vertx.kotlin.core.shareddata.getAwait
import io.vertx.kotlin.core.shareddata.putIfAbsentAwait
import io.vertx.kotlin.coroutines.awaitResult

// SharedData extensions
suspend fun <K, V> AsyncMap<K, V>.entriesAwait(): Map<K, V> {
    return awaitResult{
        this.entries(it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.getOrComputeAwait(key: K, ifNotPresent: suspend (K) -> V): V {
    var value = this.getAwait(key)
    if (value == null) {
        value = ifNotPresent(key)
        this.putIfAbsentAwait(key, value)
        value = this.getAwait(key)
    }
    return value!!
}

suspend fun <K, V> AsyncMap<K, V>.getOrComputeAwait(key: K, ttl: Long, ifNotPresent: suspend (K) -> V): V {
    var value = this.getAwait(key)
    if (value == null) {
        value = ifNotPresent(key)
        this.putIfAbsentAwait(key, value, ttl)
        value = this.getAwait(key)
    }
    return value!!
}

suspend fun <K, V> AsyncMap<K, V>.keysAwait(): Set<K> {
    return awaitResult{
        this.keys(it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.valuesAwait(): List<V> {
    return awaitResult{
        this.values(it)
    }
}

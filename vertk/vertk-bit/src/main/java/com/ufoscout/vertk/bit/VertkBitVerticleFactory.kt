package com.ufoscout.vertk.bit

import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.core.Koin

class VertkBitVerticleFactory(val koin: Koin) : VerticleFactory {

    companion object {
        val PREFIX = "vertk-bit"
    }

    override fun prefix(): String {
        return PREFIX
    }

    override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
        val className = VerticleFactory.removePrefix(verticleName)
        val clazz = classLoader.loadClass(className)

        return koin.get<Verticle>(clazz = clazz.kotlin, qualifier = null, parameters = null)
    }

}
package com.ufoscout.vertk.kodein

import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.core.Koin

class VertkKodeinVerticleFactory(val koin: Koin) : VerticleFactory {

    companion object {
        val PREFIX = "vertk-koin"
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
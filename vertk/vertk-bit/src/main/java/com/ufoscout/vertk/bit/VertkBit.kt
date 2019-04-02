package com.ufoscout.vertk.bit

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import org.koin.Logger.SLF4JLogger
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import org.koin.dsl.module

object VertkBit {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun start(vertx: Vertx,
                      vararg modules: VertkBitModule): Koin {

        log.info("Vertxk initialization start...")

        log.info("Vertxk bit modules creations...")

        val coreModule = module {
            single { vertx }
            single { vertx.eventBus() }
            single { vertx.fileSystem() }
            single { vertx.sharedData() }
        }

        val koinModules = mutableListOf(coreModule)
        for (module in modules) {
            koinModules.add(module.module())
        }

        val koin = koinApplication {
            // enable INFO logger
            //useLogger()
            // load Koin modules
            //loadKoinModules(*koinModules)
        }.logger(logger = SLF4JLogger())
                .modules(koinModules)
                .koin

        log.info("Vertxk bit modules created successfully")

        vertx.registerVerticleFactory(VertkBitVerticleFactory(koin))

        log.info("Initialize VertkBitModule...")
        for (module in modules) {
            log.info("Initialize VertkBitModule [${module.javaClass.name}]")
            module.onInit(vertx, koin)
        }

        log.info("Vertxk VertkbitModules initialization completed successfully")

        return koin
    }


}
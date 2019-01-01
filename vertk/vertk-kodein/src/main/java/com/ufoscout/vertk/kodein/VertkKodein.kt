package com.ufoscout.vertk.kodein

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import org.koin.Logger.SLF4JLogger
import org.koin.core.Koin
import org.koin.core.context.loadKoinModules
import org.koin.dsl.koinApplication
import org.koin.dsl.module

object VertkKodein {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun start(vertx: Vertx,
                      vararg modules: VertkKodeinModule): Koin {

        log.info("Vertxk initialization start...")

        log.info("Vertxk kodein modules creations...")

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

        log.info("Vertxk kodein modules created successfully")

        vertx.registerVerticleFactory(VertkKodeinVerticleFactory(koin))

        log.info("Initialize VertkKodeinModule...")
        for (module in modules) {
            log.info("Initialize VertkKodeinModule [${module.javaClass.name}]")
            module.onInit(vertx, koin)
        }

        log.info("Vertxk VertkKodeinModules initialization completed successfully")

        return koin
    }


}
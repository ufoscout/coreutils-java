package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import org.koin.core.Koin

class RouterModule(val routerConfig: RouterConfig, val httpServerOptions: HttpServerOptions): VertkKodeinModule {

    override fun module() = org.koin.dsl.module {
            single { routerConfig }
            single<WebExceptionService> { WebExceptionServiceImpl() }
            single<RouterService> { RouterServiceImpl.new(get(), httpServerOptions, get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        koin.get<RouterService>().start()
    }

}

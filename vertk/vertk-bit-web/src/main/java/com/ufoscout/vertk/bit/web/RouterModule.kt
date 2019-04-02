package com.ufoscout.vertk.bit.web

import com.ufoscout.vertk.bit.VertkBitModule
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import org.koin.core.Koin

class RouterModule(val routerConfig: RouterConfig, val httpServerOptions: HttpServerOptions): VertkBitModule {

    override fun module() = org.koin.dsl.module {
            single { routerConfig }
            single<WebExceptionService> { WebExceptionServiceImpl() }
            single<RouterService> { RouterServiceImpl.new(get(), httpServerOptions, get(), get()) }
    }

    override suspend fun onInit(vertx: Vertx, koin: Koin) {
        koin.get<RouterService>().start()
    }

}

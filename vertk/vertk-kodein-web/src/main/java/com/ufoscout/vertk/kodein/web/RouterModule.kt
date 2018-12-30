package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance

class RouterModule(val routerConfig: RouterConfig, val httpServerOptions: HttpServerOptions): VertkKodeinModule {

    override fun module(vertx: Vertx) = Kodein.Module {
            bind<RouterConfig>() with eagerSingleton { routerConfig }
            bind<WebExceptionService>() with eagerSingleton { WebExceptionServiceImpl() }
            bind<RouterService>() with eagerSingleton { RouterServiceImpl.new(instance(), httpServerOptions, instance(), instance()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        kodein.direct.instance<RouterService>().start()
    }

}

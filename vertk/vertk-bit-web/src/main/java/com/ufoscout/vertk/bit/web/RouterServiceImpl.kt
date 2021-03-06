package com.ufoscout.vertk.bit.web

import com.ufoscout.coreutils.validation.ValidationException
import com.ufoscout.vertk.web.endWithJson
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import java.util.*

class RouterServiceImpl private constructor(private val routerConfig: RouterConfig,
                                            private val httpServerOptions: HttpServerOptions,
                                            private val vertx: Vertx,
                                            private val webExceptionService: WebExceptionService) : RouterService {

    companion object {
        fun new(routerConfig: RouterConfig,
                httpServerOptions: HttpServerOptions,
                vertx: Vertx,
                webExceptionService: WebExceptionService): RouterService {

            webExceptionService.registerTransformer<BadRequestException> { ex -> WebException(cause = ex, code = 400, message = ex.message!!) }
            webExceptionService.registerTransformer<ValidationException> { ex -> WebException(cause = ex, code = 422, message = ex.message!!, details = ex.violations) }

            val router = RouterServiceImpl(routerConfig, httpServerOptions, vertx, webExceptionService)
            return router
        }
    }


    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val mainRouter = Router.router(vertx);

    init {
        mainRouter.route().failureHandler { handleFailure(it) }
    }

    override fun router(): Router {
        val router = Router.router(vertx)
        mainRouter.mountSubRouter(routerConfig.subRouterMountPoint, router)
        return router
    }

    override suspend fun start() {
        val port = vertx.createHttpServer(httpServerOptions)
                .requestHandler(mainRouter)
                .listenAwait(routerConfig.port).actualPort()
        logger.info("Router created and listening on port ${port}")
    }

    private fun handleFailure(context: RoutingContext) {
        logger.info("Handling failure")
        val exception = context.failure()
        val response = context.response()

        if (exception is WebException) {
            reply(response, exception)
        } else {
            val webEx = webExceptionService.get(exception)
            if (webEx != null) {
                reply(response, webEx)
            } else {
                var statusCode = context.statusCode()
                if (statusCode < 0) {
                    statusCode = 500
                }
                val uuid = UUID.randomUUID().toString()
                val message = "Error code: " + uuid
                logger.error(uuid + " : " + exception.message, exception)
                response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, message, WebException.DEFAULT_DETAILS))
            }
        }

    }

    private fun reply(response: HttpServerResponse, exception: WebException) {
        logger.error(exception.message, exception)
        val statusCode = exception.statusCode()
        response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, message(exception), exception.details()))
    }

    private fun message(exception: WebException): String {
        if (exception.message!=null) {
            return exception.message
        }
        return ""
    }

}

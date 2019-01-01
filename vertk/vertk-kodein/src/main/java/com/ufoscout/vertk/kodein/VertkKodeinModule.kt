package com.ufoscout.vertk.kodein

import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import org.koin.core.Koin
import org.koin.core.module.Module

interface VertkKodeinModule {

    /**
     * Return a new [Module] instance to be imported before
     * the [VertxkModule] is initialized.
     */
    fun module(): Module

    /**
     * Initialization entry point for the the [VertxkModule].
     * All [Verticle]s that belong to the module should be deployed here.
     */
    suspend fun onInit(vertx: Vertx, koin: Koin)

}

/**
 * Deploy a [Verticle] in a [Vertx] instance using
 * and [VertkKodeinVerticleFactory] factory.
 *
 * @param options  the deployment options.
 */
inline suspend fun <reified T : Verticle> Vertx.deployKodeinVerticleAwait(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    this.deployVerticleAwait(VertkKodeinVerticleFactory.PREFIX + ":" + T::class.java.canonicalName, deploymentOptions)
}

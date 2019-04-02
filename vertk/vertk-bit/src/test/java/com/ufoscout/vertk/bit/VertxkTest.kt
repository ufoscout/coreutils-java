package com.ufoscout.vertk.bit

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.bit.stub.StubModule
import com.ufoscout.vertk.bit.stub.VertxKComponentImpl
import com.ufoscout.vertk.bit.stub.VertxKVerticleImpl
import io.vertx.core.DeploymentOptions
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class VertxkTest: BaseTest() {

    @BeforeEach
    fun setUp() {
        VertxKComponentImpl.RESET();
        VertxKVerticleImpl.RESET();
    }

    @Test
    fun shouldDeploy() = runBlocking<Unit> {

        VertkBit.start(vertx = vertk, modules = *arrayOf(StubModule()))

        assertTrue(VertxKComponentImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKComponentImpl.NAME)
        assertEquals(1, VertxKComponentImpl.COUNT.get())

        assertTrue(VertxKVerticleImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKVerticleImpl.NAME)
        assertEquals(1, VertxKVerticleImpl.COUNT.get())

    }

    @Test
    fun shouldDeployWithDeploymentOptions() = runBlocking<Unit> {

        val instances = Random().nextInt(10) + 1
        val deploymentOptions = DeploymentOptions().setInstances(instances)


        VertkBit.start(vertx = vertk, modules = *arrayOf(StubModule(deploymentOptions = deploymentOptions)))
        assertTrue(VertxKComponentImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKComponentImpl.NAME)
        assertEquals(1, VertxKComponentImpl.COUNT.get())

        assertTrue(VertxKVerticleImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKVerticleImpl.NAME)
        assertEquals(instances, VertxKVerticleImpl.COUNT.get())

    }
}
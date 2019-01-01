package com.ufoscout.vertk.kodein

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.kodein.stub.SimpleVerticle
import com.ufoscout.vertk.kodein.stub.VerticleWithDependencies
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

import java.util.*

class VertxkKodeinVerticleFactoryTest: BaseTest() {

    @Test
    fun shouldReturnTheExpectedPrefix() {
        val koin = koinApplication {}.modules(listOf()).koin
        val factory = VertkKodeinVerticleFactory(koin)
        assertEquals(VertkKodeinVerticleFactory.PREFIX, factory.prefix())
    }

    @Test
    fun shouldCreateASimpleVerticleInstance() {
        val module = module {
            single { SimpleVerticle() }
        }
        val koin = koinApplication {}.modules(listOf(module)).koin
        val factory = VertkKodeinVerticleFactory(koin)

        val instance = factory.createVerticle(SimpleVerticle::class.java!!.getName(), this.javaClass.classLoader)
        assertNotNull(instance)
        assertTrue(instance is SimpleVerticle)
    }

    @Test
    fun shouldCreateAVerticleInstanceWithDependencies() {
        val name = UUID.randomUUID().toString()

        val module = module {
            single<String> { name }
            factory { VerticleWithDependencies(get()) }
        }
        val koin = koinApplication {}.modules(listOf(module)).koin
        val factory = VertkKodeinVerticleFactory(koin)

        val instance = factory.createVerticle(VerticleWithDependencies::class.java!!.getName(), this.javaClass.classLoader)
        assertNotNull(instance)
        assertTrue(instance is VerticleWithDependencies)

        assertEquals(name, (instance as VerticleWithDependencies).name)

    }


}
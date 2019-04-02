package com.ufoscout.vertk.bit

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.bit.stub.SimpleVerticle
import com.ufoscout.vertk.bit.stub.VerticleWithDependencies
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

import java.util.*

class VertxkBitVerticleFactoryTest: BaseTest() {

    @Test
    fun shouldReturnTheExpectedPrefix() {
        val koin = koinApplication {}.modules(listOf()).koin
        val factory = VertkBitVerticleFactory(koin)
        assertEquals(VertkBitVerticleFactory.PREFIX, factory.prefix())
    }

    @Test
    fun shouldCreateASimpleVerticleInstance() {
        val module = module {
            single { SimpleVerticle() }
        }
        val koin = koinApplication {}.modules(listOf(module)).koin
        val factory = VertkBitVerticleFactory(koin)

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
        val factory = VertkBitVerticleFactory(koin)

        val instance = factory.createVerticle(VerticleWithDependencies::class.java!!.getName(), this.javaClass.classLoader)
        assertNotNull(instance)
        assertTrue(instance is VerticleWithDependencies)

        assertEquals(name, (instance as VerticleWithDependencies).name)

    }


}
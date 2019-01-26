package com.ufoscout.coreutils.auth

import org.junit.jupiter.api.Test

import java.util.*
import org.junit.jupiter.api.Assertions.assertThrows

class AuthExtTest : BaseTest() {

    @Test
    fun shouldMatchAll() {
        val user = Auth(110L, "name", arrayOf("ADMIN", "USER"))
        val obj = Owneable(110L)
        val authContext = AuthContext(user, Dec())

        authContext.isAuthenticated
                .all(
                        { it.isOwnerWithRoles(obj, "USER") },
                        { it.hasAllRoles("ADMIN", "USER") }
                )
    }

    @Test
    fun shouldNotMatchAll() {
        assertThrows(UnauthorizedException::class.java
        ) {
            val user = Auth(110L, "name", arrayOf("USER"))
            val obj = Owneable(110L)
            val authContext = AuthContext(user, Dec())

            authContext.isAuthenticated
                    .all(
                            { it.isOwner(obj) },
                            { it.hasAllRoles("ADMIN", "USER") }
                    )
        }
    }

    @Test
    fun shouldMatchAny() {
        val user = Auth(110L, "name", arrayOf("ADMIN", "USER"))
        val obj = Owneable(220L)
        val authContext = AuthContext(user, Dec())

        authContext.isAuthenticated
                .any(
                        { it.isOwnerWithRoles(obj, "USER") },
                        { it.hasAnyRole("ADMIN", "ONE", "TWO") }
                )
    }

    @Test
    fun shouldNotMatchAny() {
        assertThrows(UnauthorizedException::class.java
        ) {
            val user = Auth(110L, "name", arrayOf("ADMIN", "USER"))
            val obj = Owneable(220L)
            val authContext = AuthContext(user, Dec())

            authContext.isAuthenticated
                    .any(
                            { it.isOwner(obj) },
                            { it.hasAnyPermission("ADMIN") }
                    )
        }
    }

    internal inner class Dec @JvmOverloads constructor(private val permissions: Map<String, List<String>> = HashMap()) : RolesProvider {

        override fun getAll(): List<Role>? {
            return null
        }

        override fun getByName(vararg userRoles: String): List<Role> {
            val result = ArrayList<Role>()

            for (userRole in userRoles) {
                val userPerms = ArrayList<String>()
                for ((key, value) in permissions) {
                    if (value.contains(userRole)) {
                        userPerms.add(key)
                    }
                }
                result.add(Role(userRole, userPerms.toTypedArray()))
            }
            return result
        }
    }


    internal inner class Owneable(private val ownerId: Long?) : Owned {

        override fun getOwnerId(): Long? {
            return this.ownerId
        }
    }

}
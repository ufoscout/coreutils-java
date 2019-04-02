package com.ufoscout.vertk

import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.vertk.bit.AuthTestModule
import com.ufoscout.vertk.bit.VertkBit
import com.ufoscout.vertk.bit.auth.AuthModule
import com.ufoscout.vertk.bit.json.JsonModule
import com.ufoscout.vertk.bit.web.RouterConfig
import com.ufoscout.vertk.bit.web.RouterModule
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import io.vertx.kotlin.core.closeAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.koin.core.Koin
import java.io.IOException
import java.net.ServerSocket

abstract class BaseIT : BaseTest() {

    companion object {

        private var vertk: Vertx? = null
        private val port: Int = getFreePort()
        private var koin: Koin? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            System.setProperty("server.port", port.toString())
            vertk = Vertx.vertx()

            koin = VertkBit.start(
                    vertk!!,
                    AuthModule(JwtConfig("secretslfhsadkfhadkfhakjfhawkfhawjfhawkfhaksfhakhwith9t249tyq43tq3tph53qcq98wrhpc924cthrw9ptcqh29ch5q29pthcq249ptheighcqn29cthor"
                            , "HS512", 60)),
                    JsonModule(),
                    AuthTestModule(),
                    RouterModule(RouterConfig(port), HttpServerOptions())
            )

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            vertk!!.closeAwait()
        }

        @Synchronized private fun getFreePort(): Int {
            try {
                ServerSocket(0).use { socket ->
                    socket.reuseAddress = true
                    return socket.localPort
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

    }

    protected fun port(): Int = port

    protected fun vertk() = vertk!!

    protected fun koin(): Koin = koin!!

}

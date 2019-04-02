package com.ufoscout.vertk.bit.stub

import java.util.concurrent.atomic.AtomicInteger

class VertxKComponentImpl {

    constructor(name : String) {

        println("VertxKComponentImpl instantiated with name: [$name]")

        STARTED = true
        NAME = name
        COUNT.getAndIncrement()
    }

    companion object {
        var STARTED = false
        var NAME = ""
        var COUNT = AtomicInteger(0)

        fun RESET() {
            STARTED = false
            NAME = ""
            COUNT = AtomicInteger(0)
        }
    }

}
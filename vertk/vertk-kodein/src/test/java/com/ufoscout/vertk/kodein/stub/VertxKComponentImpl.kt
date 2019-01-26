package com.ufoscout.vertk.kodein.stub

import java.util.concurrent.atomic.AtomicInteger

class VertxKComponentImpl {

    constructor(name : String) {
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
package org.droba.mutant

import org.droba.mutant.dummy.DummyEngine
import org.droba.mutant.pluggables.HaloEngine

class Halo {

    companion object Builder {
        fun setup(init: Halo.() -> Unit) {
            val halo = Halo()
            halo.init()
            halo.validate()
            halo.start()
        }
        fun middleWare(middle : ((MutantRequest) -> MutantResponse) -> (MutantRequest) -> MutantResponse) = middle
    }

    var host = "localhost"
    var port = 8080

    var engine  : HaloEngine = DummyEngine()
    var handler : ((MutantRequest) -> MutantResponse)? = null

    var WSsendText : ((String) -> Unit)? = null

    inline fun wrap(middleWare: ((MutantRequest) -> MutantResponse) -> (MutantRequest) -> MutantResponse) {
        if (handler == null)        throw RuntimeException("In Halo setup you MUST specify a handler.")
        handler = middleWare(handler!!)
    }

    private fun validate() {
        if (handler == null)        throw RuntimeException("In Halo setup you MUST specify a handler.")
        if (engine is DummyEngine)  throw RuntimeException("In Halo setup you MUST specify an engine.")
    }

    private fun start() = engine.startup(host, port, this)
}
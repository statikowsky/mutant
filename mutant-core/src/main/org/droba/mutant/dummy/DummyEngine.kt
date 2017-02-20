package org.droba.mutant.dummy

import org.droba.mutant.Halo
import org.droba.mutant.pluggables.HaloEngine

class DummyEngine : HaloEngine {
    override fun startup(host: String, port: Int, halo: Halo) {
        throw UnsupportedOperationException("No engine! Did you forget to setup an engine for Halo?")
    }
}
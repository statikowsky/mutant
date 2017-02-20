package org.droba.mutant.pluggables

import org.droba.mutant.Halo

interface HaloEngine {
    fun startup(host: String, port: Int, halo: Halo)
}



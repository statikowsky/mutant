package org.droba.mutantAbominablePasture.controllers.pasture

import org.droba.mutant.Mutant.Companion.act
import org.droba.mutant.MutantHalt

object Creatures {

    val index = act {
        "All these beautiful creatures!"
    }

    val get = act {
        throw MutantHalt("I dont know how to get this!")
    }

    val create = act {
        throw MutantHalt("I dont know how to create this!")
    }

    val update = act {
        throw MutantHalt("I dont know how to update this!")
    }

    val delete = act {
        throw MutantHalt("I dont know how to delete this!")
    }
}

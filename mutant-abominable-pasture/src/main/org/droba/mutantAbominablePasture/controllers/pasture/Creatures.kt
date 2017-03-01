package org.droba.mutantAbominablePasture.controllers.pasture

import org.droba.mutant.Action
import org.droba.mutant.MutantHalt

object Creatures {

    val index : Action = {
        "All these beautiful creatures!"
    }

    val get : Action = {
        throw MutantHalt("I dont know how to get this!")
    }

    val create : Action  = {
        throw MutantHalt("I dont know how to create this!")
    }

    val update : Action = {
        throw MutantHalt("I dont know how to update this!")
    }

    val delete : Action = {
        throw MutantHalt("I dont know how to delete this!")
    }
}

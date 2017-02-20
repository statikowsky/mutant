package org.droba.mutant

class MutantHalt(val status: Int, val description: String) : Exception(description) {
    constructor()                           : this(500,     "There was a Mutant error (internal server error)")
    constructor(errorDescription: String)   : this(500,     errorDescription)
    constructor(status: Int)                : this(status,  "Mutant halt")
}

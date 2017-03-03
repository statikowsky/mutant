package org.droba.mutant.pluggables

interface MutantSession {
    fun read(key: String)
    fun write(key: String, value: String)
    fun delete(key: String)
}

package org.droba.mutant

fun M.ok() = MutantResponse()

fun M.halt(status : Int) {
    if (status == 200)
        throw MutantHalt(status, "")
    else
        throw MutantHalt(status)
}

fun M.halt(status: Int, errorDescription: String) {
    throw MutantHalt(status, errorDescription)
}

fun M.redirect(to: String) = MutantResponse(
            responseCode = 302,
            headers = mutableMapOf("Location" to to)
)
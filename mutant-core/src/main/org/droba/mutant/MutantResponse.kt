package org.droba.mutant

data class MutantResponse (
        var contentType: String = "text/html",
        var content: String = "",
        var responseCode: Int = 200,
        var responseMessage: String = "",
        var headers: MutableMap<String, String> = mutableMapOf()
)

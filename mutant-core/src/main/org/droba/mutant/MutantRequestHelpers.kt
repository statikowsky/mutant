package org.droba.mutant

fun MutantRequest.paramOrThrow(paramName: String) = params[paramName]
        ?: throw MutantHalt("Param `$paramName` not found")

fun MutantRequest.pathParamOrThrow(pathParamName: String) = pathParams[pathParamName]
        ?: throw  MutantHalt("Pathparam `$pathParamName` not found")

fun MutantRequest.multiParamOrThrow(multiParamName: String) = multiParams[multiParamName]
        ?: throw MutantHalt("Multiparam `$multiParamName` not found")

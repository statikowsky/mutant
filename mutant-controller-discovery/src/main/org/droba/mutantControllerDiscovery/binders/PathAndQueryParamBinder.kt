package org.droba.mutantControllerDiscovery.binders

import kotlin.reflect.KType

interface PathAndQueryParamBinder {
    fun bindPathParam(value: String, type: KType) : Any
}

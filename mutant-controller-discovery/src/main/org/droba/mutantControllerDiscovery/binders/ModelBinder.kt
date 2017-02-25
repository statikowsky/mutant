package org.droba.mutantControllerDiscovery.binders

import org.droba.mutant.MutantRequest
import kotlin.reflect.KType

interface ModelBinder {
    fun bindModel(type: KType, req: MutantRequest) : Any
}

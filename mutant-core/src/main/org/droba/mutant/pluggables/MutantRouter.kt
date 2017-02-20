package org.droba.mutant.pluggables

import org.droba.mutant.M
import org.droba.mutant.Method
import org.droba.mutant.MutantRequest

interface MutantRouter {
    fun registerRoute(method: Method, route: String, action: M.() -> Any)
    fun matchToAction(req: MutantRequest): M.() -> Any
}

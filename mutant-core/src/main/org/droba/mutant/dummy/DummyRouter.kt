package org.droba.mutant.dummy

import org.droba.mutant.M
import org.droba.mutant.Method
import org.droba.mutant.MutantRequest
import org.droba.mutant.pluggables.MutantRouter

class DummyRouter : MutantRouter {

    override fun registerRoute(method: Method, route: String, action: M.() -> Any) {
        throw UnsupportedOperationException("No router! Did you forget to setup a router for Mutant?")
    }

    override fun matchToAction(req: MutantRequest): M.() -> Any {
        throw UnsupportedOperationException("No router! Did you forget to setup a router for Mutant?")
    }
}

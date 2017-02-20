package org.droba.mutantAbominablePasture.middleware

import org.droba.mutant.Halo

object AddMutatedByMutantHeader {

    val middleware = Halo.middleWare { handler ->
        { req ->
            val res = handler(req)
            res.headers.put("X-Mutated-By", "Mutant")
            res
        }
    }

}

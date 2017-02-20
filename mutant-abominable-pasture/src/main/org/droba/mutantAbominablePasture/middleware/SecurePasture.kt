package org.droba.mutantAbominablePasture.middleware

import org.droba.mutant.Halo
import org.droba.mutant.MutantResponse

object SecurePasture {

    val middleware = Halo.middleWare { handler ->
        { req ->

            val res = if (!req.path.contains("pasture") || req.path == "/pasture/login")
                handler(req)
            else
                MutantResponse(
                        responseCode = 302,
                        headers = mutableMapOf("Location" to "/pasture/login")
                )

            res
        }
    }
}

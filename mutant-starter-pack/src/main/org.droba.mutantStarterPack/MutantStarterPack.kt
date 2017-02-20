package org.droba.mutantStarterPack

import org.droba.mutant.Halo
import org.droba.mutant.Mutant
import org.droba.mutantGsonJsonRenderer.GsonJsonRenderer
import org.droba.mutantGsonJsonRenderer.json
import org.droba.mutantHandlebarsTemplateRenderer.view
import org.droba.mutantJettyEmbeddedServlet.Jetty
import org.droba.mutantNaiveRouter.NaiveRouter

fun mutant(mutantConfig: Mutant.() -> Unit) {
    haloMutant(mutantConfig)
}

fun haloMutant(mutantConfig: Mutant.() -> Unit, haloConfig: (Halo.() -> Unit)? = null) {

    val mutant = Mutant.setup {

        router = NaiveRouter()

        onError = renderMessage { message ->
                if (req.isJson) json(message)
                else            view("error" to message)
        }

        on404 = renderMessage { message ->
                if (req.isJson) json(message)
                else            view("404")
        }

        addResultConverter(GsonJsonRenderer.processJsonObjectActionResult)

        mutantConfig()
    }

    Halo.setup {
        engine  = Jetty()
        handler = mutant.handler

        if (haloConfig != null) haloConfig()
    }
}

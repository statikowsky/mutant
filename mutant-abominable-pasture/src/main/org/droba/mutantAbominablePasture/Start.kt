package org.droba.mutantAbominablePasture

import com.github.salomonbrys.kotson.jsonObject
import org.droba.mutant.*
import org.droba.mutantAbominablePasture.dtos.ExampleUser
import org.droba.mutantAbominablePasture.dtos.Test
import org.droba.mutantAbominablePasture.middleware.AddMutatedByMutantHeader
import org.droba.mutantAbominablePasture.middleware.SecurePasture
import org.droba.mutantAbominablePasture.views.Hello
import org.droba.mutantAbominablePasture.views.TestForm
import org.droba.mutantAbominablePasture.views.Thing
import org.droba.mutantControllerDiscovery.discoverControllersAndModels
import org.droba.mutantGsonJsonRenderer.into
import org.droba.mutantGsonJsonRenderer.json
import org.droba.mutantHandlebarsTemplateRenderer.view
import org.droba.mutantSpringloadedSupport.registerReloadNotifier
import org.droba.mutantStarterPack.haloMutant

fun main(args: Array<String>) {

    haloMutant({

        discoverControllersAndModels()

        get("/")
            { Hello.view() }

        get("/json")
            { json(Test("Hello")) }

        get("/json2")
            {
                jsonObject(
                        "mutant" to "hello",
                        "person" to "atoi"
                )
            }

        post("/json3")
            {
                json(req into ExampleUser::class)
            }

        post("/json4")
            {
                json(req.into<ExampleUser>())
            }

        get("/halt")
            { halt(200) }

        get("/error")
            {
                throw FakeException("Don't be alarmed, this is just a test :)")
            }

        get("/thing/:id")
            {
                val id = req.pathParams[":id"]
                    ?: throw MutantHalt(400, "Unknown id")

                Thing.show(id)
            }

        get("/thong/:id/:subId")
            {
                val id = req.pathParams[":id"]
                        ?: throw MutantHalt(400, "Unknown id")

                val subId = req.pathParams[":subId"]
                        ?: throw MutantHalt(400, "Unknown subid")

                Thing.show(id + " + " + subId)
            }

        get("/ip")
            { Thing.show(req.ip) }

        get("/path")
            { Thing.show(req.path) }

        get("/user-agent")
            {
                val userAgent = req.headers["User-Agent"] ?: "None found :("
                Thing.show(userAgent)
            }

        get("/param")
            {
                val query = req.params["query"] ?: "None found :("
                Thing.show(query)
            }

        get("/param-or-throw")
            {
                val query = req.paramOrThrow("query")
                Thing.show(query)
            }

        get("/path-param-or-throw/:pathParam")
            {
                val pathParam = req.pathParamOrThrow(":pathParam")
                Thing.show(pathParam)
            }

        get("/multi-param")
            {
                val multiParams = req.multiParams["query"]?.joinToString()
                        ?: "None found :("

                Thing.show(multiParams)
            }

        get("/form-test")
            { TestForm.view() }

        get("/kotlin-html-test")
            { Hello.view() }

        get("/redirect")
            { redirect("/error") }
    },
    {
        wrap(SecurePasture.middleware)
        wrap(AddMutatedByMutantHeader.middleware)

        registerReloadNotifier("org.droba.mutantAbominablePasture")
    })
}

class FakeException(message: String) : Exception(message)

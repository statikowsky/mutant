package org.droba.mutantAbominablePasture.controllers.pasture

import com.github.salomonbrys.kotson.jsonObject
import mu.KotlinLogging
import org.droba.mutant.*
import org.droba.mutantAbominablePasture.dtos.CaretakerDto
import org.droba.mutantAbominablePasture.dtos.PillDto
import org.droba.mutantControllerDiscovery.Get
import org.droba.mutantControllerDiscovery.Path
import org.droba.mutantControllerDiscovery.Post
import org.droba.mutantGsonJsonRenderer.GsonJsonRenderer
import org.droba.mutantGsonJsonRenderer.json

class Caretakers {

    private val log = KotlinLogging.logger { }

    val get : M.(Int) -> Any =
        { id -> id }

    val index : Action =
        { "Hello from Caretakers!" }

    val create : M.(CaretakerDto) -> Any =
        { dto ->
            log.info { "Hello there ! ${dto.name} ${dto.surname}" }
            json(dto)
        }

    val update : M.(CaretakerDto) -> Any =
        { dto -> json(dto) }

    val delete : M.(Int) -> Any =
        { id ->
            log.info { "Id: `$id`" }
            ok()
        }

    @Get val search : M.(String) -> Any =
        { query -> "You searched for caretakers with `$query`" }

    @Post @Path("/:caretakerId/:boxName/create-pill")
    val createPillForCaretaker : M.(Int, String, PillDto) -> Any =
        { caretakerId, boxName, pillDto ->

            val pill = GsonJsonRenderer.gson.toJsonTree(pillDto)

            jsonObject(
                "caretakerId" to caretakerId,
                "boxName" to boxName,
                "pill" to pill
            )
        }
}

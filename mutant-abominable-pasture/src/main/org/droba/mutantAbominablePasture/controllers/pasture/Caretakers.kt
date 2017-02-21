package org.droba.mutantAbominablePasture.controllers.pasture

import mu.KotlinLogging
import org.droba.mutant.*
import org.droba.mutant.Mutant.Companion.act
import org.droba.mutantAbominablePasture.dtos.CaretakerDto
import org.droba.mutantAbominablePasture.dtos.PillDto
import org.droba.mutantControllerDiscovery.Path
import org.droba.mutantControllerDiscovery.Post

object Caretakers {

    private val log = KotlinLogging.logger { }

    val get : M.(Int) -> Any = {
        id -> ok()
    }

    val index = act {
        ok()
    }

    val create : M.(CaretakerDto) -> Any = {
        dto -> ok()
    }

    val update : M.(CaretakerDto) -> Any = {
        dto -> ok()
    }

    val delete : M.(Int) -> Any = {
        id -> ok()
    }

    @Post @Path("/:id/create-pill")
    val createPillForCaretaker : M.(Int, PillDto) -> Any = {
        caretakerId, pillDto ->
            log.debug { "caretakerId: $caretakerId" }
            ok()
    }
}

package org.droba.mutantAbominablePasture.controllers

import org.droba.mutant.Action
import org.droba.mutant.MutantHalt
import org.droba.mutantAbominablePasture.views.PastureViews
import org.droba.mutantControllerDiscovery.Get
import org.droba.mutantControllerDiscovery.Post
import org.droba.mutantControllerDiscovery.Singular

@Singular object Pasture {

    val get : Action =
        { "Hello from a beautiful pasture!" }

    @Get val login : Action =
        {  PastureViews.login() }

    @Post val auth : Action =
        { throw MutantHalt("I dont know how to do this!") }

    @Post val logout : Action =
        { throw MutantHalt("I dont know how to logout!") }
}

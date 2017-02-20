package org.droba.mutantAbominablePasture.controllers

import org.droba.mutant.Mutant.Companion.act
import org.droba.mutant.MutantHalt
import org.droba.mutantAbominablePasture.views.PastureViews
import org.droba.mutantControllerDiscovery.Get
import org.droba.mutantControllerDiscovery.Post
import org.droba.mutantControllerDiscovery.Singular

@Singular object Pasture {

    val get = act {

        // check if logged in - if not send to log in screen!
        "Hello from a beautiful pasture!"
    }

    @Get val login = act {
        PastureViews.login()
    }

    @Post val auth = act {
        throw MutantHalt("I dont know how to do this!")
    }

    @Post val logout = act {
        throw MutantHalt("I dont know how to logout!")
    }
}

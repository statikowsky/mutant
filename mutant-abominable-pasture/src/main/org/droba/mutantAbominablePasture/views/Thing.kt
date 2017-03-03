package org.droba.mutantAbominablePasture.views

import kotlinx.html.h1
import kotlinx.html.span
import org.droba.mutantAbominablePasture.views.containers.mainDiv

object Thing  {

    fun show (thing: String) = mainDiv {
        h1("f-headline") {

            span("blue") { + "Thing:" }
            span("green") { + thing }

        }
    }
}
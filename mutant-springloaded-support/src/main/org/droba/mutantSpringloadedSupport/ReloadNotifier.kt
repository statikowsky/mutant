package org.droba.mutantSpringloadedSupport

import org.droba.mutant.Halo
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.springsource.loaded.agent.SpringLoadedPreProcessor

fun Halo.registerReloadNotifier(appPackage: String) {

    SpringLoadedPreProcessor.registerGlobalPlugin(SpringLoadedReloadEvent(this))

    Reflections(appPackage, SubTypesScanner(false))
            .getSubTypesOf(Class.forName("kotlin.jvm.internal.Lambda"))
    Reflections(appPackage, SubTypesScanner(false))
            .getSubTypesOf(Object::class.java)
}


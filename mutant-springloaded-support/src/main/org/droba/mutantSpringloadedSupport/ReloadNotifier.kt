package org.droba.mutantSpringloadedSupport

import org.droba.mutant.Halo
import org.springsource.loaded.agent.SpringLoadedPreProcessor

fun Halo.registerReloadNotifier() = SpringLoadedPreProcessor.registerGlobalPlugin(SpringLoadedReloadEvent(this))


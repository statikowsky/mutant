package org.droba.mutantSpringloadedSupport

import org.droba.mutant.Halo
import org.slf4j.LoggerFactory
import org.springsource.loaded.ReloadEventProcessorPlugin

class SpringLoadedReloadEvent(val halo: Halo) : ReloadEventProcessorPlugin {

    val log = LoggerFactory.getLogger(SpringLoadedReloadEvent::class.java)

    override fun shouldRerunStaticInitializer(typename: String?, clazz: Class<*>?, encodedTimestamp: String?): Boolean
            = false

    override fun reloadEvent(typename: String?, clazz: Class<*>?, encodedTimestamp: String?) {
        log.info("Reloaded : {}", typename)
        halo.WSsendText?.invoke("MutantWS Reload")
    }
}

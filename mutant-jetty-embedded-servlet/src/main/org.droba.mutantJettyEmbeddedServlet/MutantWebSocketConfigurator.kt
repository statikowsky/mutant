package org.droba.mutantJettyEmbeddedServlet

import org.droba.mutant.Halo
import javax.websocket.server.ServerEndpointConfig

class MutantWebSocketConfigurator(val halo : Halo) : ServerEndpointConfig.Configurator() {

    override fun <T : Any?> getEndpointInstance(endpointClass: Class<T>?): T
            = MutantWebSocket(this.halo) as T
}
